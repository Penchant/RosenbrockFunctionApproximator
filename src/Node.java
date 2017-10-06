import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Node {

    private Type nodeType;
    public double output;
    public List<Double> inputs;
    public List<Double> weights;

    public Node(Type nodeType) {
        this.nodeType = nodeType;
    }

    public List<Double> calculateOutputs() {
        final Function<Double, Double> activationFunction;

        switch(nodeType) {
            case HIDDEN:    activationFunction = logisticActivation; break;
            case RBFHIDDEN: activationFunction = logisticActivation; break;
            case INPUT:     activationFunction = linearActivation;   break;
            case OUTPUT:    activationFunction = linearActivation;   break;
            case RBFINPUT:  activationFunction = linearActivation;   break;
            default:        activationFunction = linearActivation;   break;
        }

        return Stream.iterate(0, i -> i + 1)
                .limit(inputs.size())
                .map(i -> new Double[]{activationFunction.apply(inputs.get(i)), weights.get(i)})
                .map(calculateOutput)
                .collect(Collectors.toList());
    }

    public void updateWeights(List<Double> newWeights) {
        weights = newWeights;
    }

    private double gaussianBasisFunction(double value, double musubj, double delta) {
        return Math.pow(Math.E, - Math.pow(value - musubj, 2) / (2 * delta * delta));
    }

    /**
     * Function to calculate the output for each [Node]
     * Takes in a value and a weight and multiplies them
     */
    private Function<Double[], Double> calculateOutput = values -> values[0] * values[1];

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
