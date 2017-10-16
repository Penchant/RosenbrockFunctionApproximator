import java.util.ArrayList;
import java.util.List;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Network implements Runnable {

    public List<Layer> layers = new ArrayList<>();

    private Layer inputLayer;
    private List<Example> examples;
    private int hiddenLayers;
    private int dimension;
    private int nodesPerHiddenLayer;
    private boolean isRadialBasis;
    private Double learningRate = 0.001d;

    public Network(int hiddenLayers, int nodesPerHiddenLayer, int dimension, boolean isRadialBasis, List<Example> examples) {
        this.hiddenLayers = hiddenLayers;
        this.dimension = dimension;
        this.nodesPerHiddenLayer = nodesPerHiddenLayer;
        this.isRadialBasis = isRadialBasis;

        this.examples = examples;
        if (isRadialBasis) {
            nodesPerHiddenLayer = examples.size();
            hiddenLayers = 1;
        }

        Layer.network = this;
        layers.add(new Layer(dimension, Type.INPUT));
        inputLayer = layers.get(0);

        if (!isRadialBasis) {
            for (int i = 0; i < hiddenLayers; i++) {
                layers.add(new Layer(nodesPerHiddenLayer, Type.HIDDEN));
            }
        } else {
            Layer rbfHidden = new Layer (examples.size(), Type.RBFHIDDEN);

            examples.forEach(example ->
                rbfHidden.nodes.forEach(current -> {
                    for (int k = 0; k < example.inputs.size(); k++) {
                        current.weights.set(k, example.inputs.get(k));
                    }
                    current.mu = example.outputs.get(0);
                })
            );

            layers.add(rbfHidden);
        }

        layers.add(new Layer(examples.get(0).outputs.size(), Type.OUTPUT));
    }

    @Override
    public void run() {
        while (true) {
            List<Double> output = new ArrayList<Double>();

            while (Main.shouldPause) {
                try {
                    Thread.sleep(100);
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }

            // For each example we set the input layer's node's inputs to the example value,
            // then calculate the output for that example.
            for (int i = 0; i < examples.size(); i++) {
                Example example = examples.get(i);
                Double networkOutput = forwardPropagate(example);
                output.add(networkOutput);

                System.out.println("Network predicted " + networkOutput + " for inputs of " + example.inputs.toString() + " and a correct output of " + example.outputs.get(0));

                if (Double.isNaN(networkOutput)) {
                    System.err.println("NaN");
                    System.exit(1);
                }

                backPropagate(examples.get(i).outputs);
            }

            layers.forEach(layer -> layer.nodes.forEach(node -> node.updateWeights()));

            List<Double> outputs = examples
                    .stream()
                    .map(example -> example.outputs.get(0))
                    .collect(Collectors.toList());

            System.out.println("Average error is " + calculateAverageError(output, outputs));
        }
    }

    /**
     * TODO: write a description of forward propagation
     * Used for batch updates, where all examples will have their outputs calculated
     *
     * @return A [List] containing the output for each example in the examples list.
     */
    public Double forwardPropagate(Example example) {
        Layer input = layers.get(0);

        // For each node in the input layer, set the input to the node
        input.nodes.parallelStream().forEach(node -> {
            node.inputs.clear();
            node.inputs.addAll(example.inputs);
        });

        // Calculate the output for each layer and pass it into the next layer
        for (int j = 0; j < layers.size() - 1; j++) {
            Layer currentLayer = layers.get(j);
            List<Double> outputs = currentLayer.calculateNodeOutputs();
            // If we are not at the output layer, we are going to set the
            // Next layers inputs to the current layers outputs.
            Layer nextLayer = layers.get(j + 1);
            // Grab each node in the layer
            nextLayer.nodes.parallelStream().forEach(node -> {
                // Set each node's inputs to the outputs
                node.inputs.clear();
                node.inputs.addAll(outputs);
            });
        }

        // We have hit the output and need to save it - Assume output has only one node.
        return layers.get(layers.size() - 1).calculateNodeOutputs().get(0);
    }

    /**
     * Use forwardProp to get output layer // TODO: ??????
     *
     * @param target
     */
    public void backPropagate(List<Double> target) {
        Layer currentLayer = layers.get(hiddenLayers + 1);
        Layer previousLayer = layers.get(hiddenLayers);
        List<Node> outputNodes = currentLayer.nodes;

        // Updating weights on output layer
        for (int i = 0; i < outputNodes.size(); i++) {
            Node outputNode = outputNodes.get(i);
            outputNode.delta = -1 * (target.get(i) - outputNode.output) * outputNode.output * (1 - outputNode.output);

            for (int j = 0; j < outputNode.newWeights.size(); j++) {
                double weightChange = outputNode.delta * previousLayer.nodes.get(j).output;

                if (Double.isNaN(weightChange)){
                    System.err.println("weightChange is not a number");
                }
                if (outputNode.delta == 0) {
                    System.err.println("delta is zero");
                }

                outputNode.newWeights.set(j,outputNode.newWeights.get(j) - learningRate * weightChange);
            }
        }

        // Iterating over all hidden layers to calculate weight change
        for(int x = hiddenLayers; x > 0; x--) {
            outputNodes = currentLayer.nodes;
            currentLayer = layers.get(x);
            previousLayer = layers.get(x - 1);

            // Updates the weights of each node in the layer
            for (int i = 0; i < currentLayer.nodes.size(); i++) {
                final int index = i;
                Node currentNode = currentLayer.nodes.get(i);

                double weightedDeltaSum = outputNodes.parallelStream().mapToDouble(node -> node.delta * node.weights.get(index)).sum();
                currentNode.delta = weightedDeltaSum * currentNode.output * (1 - currentNode.output);

                // Updating each weight in the node
                for (int j = 0; j < currentNode.newWeights.size(); j++) {
                    double weightChange = currentNode.delta * previousLayer.nodes.get(j).output;
                    if (Double.isNaN(weightChange)){
                        System.err.println("weightChange is not a number");

                    }

                    currentNode.newWeights.set(j, currentNode.newWeights.get(j) - learningRate * weightChange);
                }
            }
        }
    }

    public List<Double> calculateError() {return null;}

    private double calculateSigma() {
        double maxDistance = 0;

        List<Double> inputs = new Example().inputs;

        for (int i = 0; i < examples.size() ; i++) {
            double sum = 0;
            for (int j = 0; j < inputs.size(); j++) {
                double inputVal = inputs.get(j);
                double exampleVal = examples.get(i).inputs.get(j);
                sum += (inputVal - exampleVal) * (inputVal - exampleVal);
            }

            if (sum > maxDistance) {
                maxDistance = sum;
            }
        }

        return Math.sqrt(maxDistance);
    }

    /**
     * Calculates the Rosenbrock function from the given input
     * f(x) = f(x1, x1, ..., xn) = Sum over all elements of [(1-x_i)^2 + 100(x_(i+1) - (x_i)^2)^2]
     *
     * @param values Input values for the function of any dimension
     * @return The result of applying to Rosenbrock function to the given input
     */
    public static double rosenbrock(double... values) {
        return IntStream.range(0, values.length - 1)
                .boxed()
                .parallel()
                .map(i -> new Double[]{values[i], values[i + 1]})
                .mapToDouble(rosenbrock2D)
                .sum();
    }

    /**
     * Calculates the Rosenbrock function from the given 2D input
     * f(x) = f(x, y) = [(1-x)^2 + 100(y - x^2)^2]
     *
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
                .mapToDouble(i -> 0.5d * Math.pow(inputs.get(i) - outputs.get(i), 2))
                .sum();
    }

    public double calculateAverageError(List<Double> outputs, List<Double> inputs) {
        return IntStream.range(0, outputs.size())
                .mapToDouble(i -> Math.abs(inputs.get(i) - outputs.get(i)))
                .sum() / outputs.size();
    }
}
