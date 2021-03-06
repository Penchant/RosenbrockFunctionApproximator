import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Node {

    public static double sigma = 0;

    public List<Double> inputs = new ArrayList<>();
    public List<Double> weights = new ArrayList<>();
    public List<Double> newWeights = new ArrayList<>();

    public double output;
    public double mu = 0;
    public double delta;

    private Type nodeType;

    public Node(Type nodeType, int inputCount) {
        this.nodeType = nodeType;

        for (int i = 0; i < inputCount ; i++) {
            if (nodeType == Type.INPUT) {
                weights.add(1d);
            } else if (nodeType == Type.OUTPUT){
                weights.add(Math.random() * 10000);
            } else {
                weights.add(Math.random());
            }
        }

        newWeights.addAll(weights);
    }

    public double calculateOutput() {
        final Function<Double, Double> activationFunction;

        switch (nodeType) {
            case HIDDEN:    activationFunction = logisticActivation; break;
            case RBFHIDDEN: activationFunction = gaussianBasisFunction; break;
            case INPUT:
            case OUTPUT:
            case RBFINPUT:
            default:        activationFunction = linearActivation; break;
        }

        return output = activationFunction.apply(
                IntStream.range(0, inputs.size())
                        .boxed()
                        .parallel()
                        .map(i -> new Double[]{inputs.get(i), weights.get(i)})
                        .mapToDouble(calculateOutput)
                        .sum()
        );
    }

    public void updateWeights() {
        weights = new ArrayList<Double>(newWeights);
    }

    /**
     * Function to calculate the output for each [Node]
     * Takes in a value and a weight and multiplies them
     */
    private ToDoubleFunction<Double[]> calculateOutput = values -> values[0] * values[1];
    /**
     * Gaussian Basis Function (RBF Activation function)
     */
    private Function<Double, Double> gaussianBasisFunction = value -> Math.pow(Math.E, -Math.pow(value - mu, 2) / (2 * sigma * sigma));
    /**
     * Linear Activation Function
     * Returns the input
     */
    private Function<Double, Double> linearActivation = Function.identity();
    /**
     * Logistic Activation Function
     * Returns the input mapped to a sigmoidal curve
     */
    private Function<Double, Double> logisticActivation = value -> 1d / (1 + Math.pow(Math.E, -value));

    @Override
    public String toString() {
        return "Node Output: " + output;
    }

}
