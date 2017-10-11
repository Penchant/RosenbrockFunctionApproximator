import java.util.List;
import java.util.function.ToDoubleFunction;
import java.util.stream.*;

public class Network {

    private List<List<Double>> examples;
    private List<Layer> layers;

    private int hiddenLayers;
    private int inputCount;
    private int nodesPerHiddenLayer;
    private boolean isRadialBasis;

    public Network(int hiddenLayers, int nodesPerHiddenLayer, int inputCount, boolean isRadialBasis) {
        this.hiddenLayers = hiddenLayers;
        this.inputCount = inputCount;
        this.nodesPerHiddenLayer = nodesPerHiddenLayer;
        this.isRadialBasis = isRadialBasis;
    }

    public void forwardPropogate() {}

    /**
     * Use forwardProp to get output layer
     * @param outputs
     * @param inputs
     */
    public void backPropogate(List<Double> outputs, List<Double> target){
        List<Double> delta;
        double newWeight = 0;
        Layer currentLayer = layers.get(hiddenLayers+1);

            Layer previousLayer = layers.get(hiddenLayers - i);

            for (int i = 0; i < outputs.size(); i++) {
                for(Double out:outputs)
                delta.add((outputs.get(i) - target.get(i)) * outputs.get(1) * (1 - outputs.get(i)));
            }
            /**
             * Loops through all Weights attached
             */
            for(Node currentNode : previousLayer.nodes){
                //newWeight = delta* currentNode.weights;
                for(Double currentWeight : currentNode.weights){

                }
            }




    }
    public List<Double> calculateError(){return null;}
    private void kMeansCluster(int k){}
    private double calculateSigma(){return 0d;}

    /**
     * Calculates the Rosenbrock function from the given input
     * f(x) = f(x1, x1, ..., xn) = Sum over all elements of [(1-x_i)^2 + 100(x_(i+1) - (x_i)^2)^2]
     * @param values Input values for the function of any dimension
     * @return The result of applying to Rosenbrock function to the given input
     */
    private double rosenbrock(double ... values) {
        return IntStream.range(0, values.length - 2)
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
    private ToDoubleFunction<Double[]> rosenbrock2D = values -> Math.pow(Math.pow(1 - values[0], 2) + 100 * (values[1] - Math.pow(values[0], 2)), 2);

    /**
     * Calculates total error from Rosenbrock inputs and output from nodes
     * f(x) = sum(.5(expected-output)^2)
     * @param nodeOutput from calculated node output
     * @param inputs from rosenBrock
     * @return squared error result
     */
    public double calculateTotalError(List<Double> outputs, List<Double> inputs) {
        double error = 0;
        for (int i = 0; i < outputs.size(); i++){
            error = 0.5*(Math.pow((inputs.get(i)-outputs.get(i)), 2));
        }
        return error;
    }
}
