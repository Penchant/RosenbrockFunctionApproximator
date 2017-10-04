import java.util.ArrayList;
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
            case HIDDEN:    activationFunction = this::logisticActivation; break;
            case RBFHIDDEN: activationFunction = this::logisticActivation; break;
            case INPUT:     activationFunction = this::linearActivation;   break;
            case OUTPUT:    activationFunction = this::linearActivation;   break;
            case RBFINPUT:  activationFunction = this::linearActivation;   break;
            default:        activationFunction = this::linearActivation;   break;
        }

        return Stream.iterate(0, i -> i + 1)
                .limit(inputs.size())
                .map(i -> calculateOutput(activationFunction.apply(inputs.get(i)), weights.get(i)))
                .collect(Collectors.toList());
    }

    public void updateWeights(List<Double> newWeights) {
        weights = newWeights;
    }

    private double gaussianBasisFunction(double value, double musubj, double delta) {
        return Math.pow(Math.E, - Math.pow(value - musubj, 2) / (2 * delta * delta));
    }

    private double calculateOutput(double value, double weight) {
        return value * weight;
    }

    private double linearActivation(double value) {
        return value;
    }

    private double logisticActivation(double value) {
        return 1d / (1 + Math.pow(Math.E, -value));
    }

}
