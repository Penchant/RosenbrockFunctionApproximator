import java.util.List;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Node {

    public static double sigma = 0;

    private Type nodeType;
    public double output;
    public List<Double> inputs;
    public List<Double> weights;

    public double mu = 0;

    public Node(Type nodeType) {
        this.nodeType = nodeType;
    }

    public double calculateOutputs() {
        final Function<Double, Double> activationFunction;

        switch(nodeType) {
            case HIDDEN:    activationFunction = logisticActivation; break;
            case RBFHIDDEN: activationFunction = gaussianBasisFunction; break;
            case INPUT:
            case OUTPUT:
            case RBFINPUT:
            default:        activationFunction = linearActivation;   break;
        }

        return activationFunction.apply(IntStream.range(0, inputs.size())
                .boxed()
                .parallel()
                .map(i -> new Double[]{inputs.get(i), weights.get(i)})
                .mapToDouble(calculateOutput)
                .sum());
    }

    public void updateWeights(List<Double> newWeights) {
        weights = newWeights;
    }

    private Function<Double, Double> gaussianBasisFunction = value -> Math.pow(Math.E, - Math.pow(value - mu, 2) / (2 * sigma * sigma));

    /**
     * Function to calculate the output for each [Node]
     * Takes in a value and a weight and multiplies them
     */
    private ToDoubleFunction<Double[]> calculateOutput = values -> values[0] * values[1];

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

}
