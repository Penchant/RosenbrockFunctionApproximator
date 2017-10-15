
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.ToDoubleFunction;
import java.util.stream.*;

public class Network implements Runnable {

    private List<Example> examples;
    public List<Layer> layers = new ArrayList<>();

    private Layer inputLayer;
    private int hiddenLayers;
    private int dimension;
    private int nodesPerHiddenLayer;
    private boolean isRadialBasis;

    private double learningRate;

    public Network(int hiddenLayers, int nodesPerHiddenLayer, int dimension, boolean isRadialBasis, List<Example> examples) {
        this.hiddenLayers = hiddenLayers;
        this.dimension = dimension;
        this.nodesPerHiddenLayer = nodesPerHiddenLayer;
        this.isRadialBasis = isRadialBasis;

        this.examples = examples;
        if(isRadialBasis){
            nodesPerHiddenLayer = examples.size();
            hiddenLayers = 1;
        }

        Layer.network = this;
        layers.add(new Layer(dimension, Type.INPUT));
        inputLayer = layers.get(0);
        for (int i = 0; i < hiddenLayers; i++) {
            layers.add(new Layer(nodesPerHiddenLayer, isRadialBasis ? Type.RBFHIDDEN : Type.HIDDEN));
        }
        layers.add(new Layer(examples.get(0).outputs.size(), Type.OUTPUT));

    }

    @Override
    public void run(){
        boolean forever = true;
        while (forever){

            List<Double> output = new ArrayList<Double> ();

            // For each example we set the input layer's node's inputs to the example value,
            // then calculate the output for that example.
            for (int i = 0; i < examples.size (); i++) {
                try {
                    Example example = examples.get(i);
                    Double networkOutput = forwardPropagate(example);
                    output.add(networkOutput);
                    System.out.println("Network predicted " + networkOutput + " for inputs of " + example.inputs.toString() + " and a correct output of " + example.outputs.get(0));
                    backPropagate(examples.get(i).outputs);
                } catch (IllegalStateException e){
                    e.printStackTrace();
                    System.exit(1);
                }
            }

            for (Layer lay: layers) {
                for(Node node : lay.nodes){
                    node.weights = node.newWeights;
                }
            }

            List<Double> outputs = examples
                    .stream()
                    .map(example -> example.outputs.get(0))
                    .collect(Collectors.toList());

            System.out.println("Total error is " + calculateTotalError(output, outputs));
        }
    }

    /**
     * TODO: write a description of forward propagation
     * Used for batch updates, where all examples will have their outputs calculated
     * @return A [List] containing the output for each example in the examples list.
     */
    public Double forwardPropagate(Example example) throws IllegalStateException {
        Layer input = layers.get(0);

        // for each node in the input layer, set the input to the node
        for (int j = 0; j < input.nodes.size(); j++) {
            Node currentNode = input.nodes.get(j);
            currentNode.inputs.clear();
            currentNode.inputs.addAll(example.inputs);
        }

        // Calculate the output for each layer and pass it into the next layer
        for (int j = 0; j < layers.size(); j++) {
            Layer currentLayer = layers.get(j);
            List<Double> outputs = currentLayer.calculateNodeOutputs();
            // If we are not at the output layer, we are going to set the
            // next layers inputs to the current layers outputs.
            if (j != layers.size() - 1) {
                Layer nextLayer = layers.get(j + 1);
                // Grab each node in the layer
                for (int k = 0; k < nextLayer.nodes.size(); k++) {
                    Node currentNode = nextLayer.nodes.get(k);
                    currentNode.inputs.clear();
                    // set each node's inputs to the outputs
                    currentNode.inputs.addAll(outputs);
                }
            } else return outputs.get(0); // Else we have hit the output and need to save it - Assume output has only one node.
        }
        throw new IllegalStateException("Should have hit the output layer");
    }

    /**
     * Use forwardProp to get output layer // TODO: ??????
     * @param target
     */
    public void backPropagate(List<Double> target) {
        List<Double> delta = new ArrayList<Double>();
        double newWeight = 0; // TODO: Unused

        Layer currentLayer = layers.get(hiddenLayers + 1);

        Layer previousLayer = layers.get(hiddenLayers);
        List<Node> outputs = currentLayer.nodes;

        for(Node outputNode : outputs) {
            int index = outputs.indexOf(outputNode);
            delta.add((outputNode.output - target.get(index)) * outputNode.output * (1 - outputNode.output));

            /**
             * Loops through all Weights attached
             */
            for (Node currentNode : previousLayer.nodes) {
                int i = previousLayer.nodes.indexOf(currentNode);
                Double currentWeight = outputNode.newWeights.get(i);
                Double weightChange = (delta.get(0)) * currentNode.output;
                outputNode.newWeights.set(i, currentWeight - learningRate * weightChange);
            }
        }


        // Starting iteration at hidden layer
        for (int l = hiddenLayers; l>0; l--) {
            previousLayer = currentLayer;
            currentLayer = layers.get(currentLayer.id - 1);
            outputs = currentLayer.nodes;

            // Only executing on hidden layers
            if(currentLayer.layerType != Type.HIDDEN && currentLayer.layerType != Type.RBFHIDDEN)
                continue;
            // Iterating through all nodes in currentLayer
            for (Node hiddenNode : outputs) {
//                int index = outputs.indexOf(hiddenNode); // TODO: Unused
                double deltaWeightSum = 0;
                double newDelta;

                // Taking every weight attached to previous layer and summing (previous delta) * (All attached weights)
                for(double weight : hiddenNode.weights) {
                    int j = currentLayer.id;
                    deltaWeightSum += delta.get(j - 1) * weight;
                }

                newDelta = deltaWeightSum * (1 - hiddenNode.output) * hiddenNode.output;
                delta.add(newDelta);

                // Updates all weights TODO: CHANGE HARDCODED DELTA INDEX
                for (Node currentNode : previousLayer.nodes) {
                    int i = previousLayer.nodes.indexOf(currentNode);
                    double currentNewWeight = hiddenNode.newWeights.get(i);
                    Double weightChange = delta.get(layers.indexOf(currentLayer)) * currentNode.output;
                    hiddenNode.newWeights.set(i, currentNewWeight - learningRate * weightChange);
                }
            }
        }

    }

    public List<Double> calculateError(){return null;}
    private double calculateSigma(){return 0d;}

    /**
     * Calculates the Rosenbrock function from the given input
     * f(x) = f(x1, x1, ..., xn) = Sum over all elements of [(1-x_i)^2 + 100(x_(i+1) - (x_i)^2)^2]
     * @param values Input values for the function of any dimension
     * @return The result of applying to Rosenbrock function to the given input
     */
    public static double rosenbrock(double ... values) {
        return IntStream.range(0, values.length - 1)
                .boxed()
                .parallel()
                .map(i -> new Double[] {values[i], values[i + 1]})
                .mapToDouble(rosenbrock2D)
                .sum();
    }

    /**
     * Calculates the Rosenbrock function from the given 2D input
     * f(x) = f(x, y) = [(1-x)^2 + 100(y - x^2)^2]
     * @param values 2D input values for the function
     * @return The result of applying to Rosenbrock function to the given input
     */
    private static ToDoubleFunction<Double[]> rosenbrock2D = values -> Math.pow(Math.pow(1 - values[0], 2) + 100 * (values[1] - Math.pow(values[0], 2)), 2);

    /**
     * Calculates total error from Rosenbrock inputs and output from nodes
     * f(x) = sum(.5(expected-output)^2)
     * @param outputs from calculated node output
     * @param inputs from rosenBrock
     * @return squared error result
     */
    public double calculateTotalError(List<Double> outputs, List<Double> inputs) {
        return IntStream.range(0, outputs.size())
                .mapToDouble(i -> 0.5d*(Math.pow((inputs.get(i)-outputs.get(i)), 2)))
                .sum();
    }
}
