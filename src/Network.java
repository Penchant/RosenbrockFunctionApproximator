import java.util.List;
import java.util.function.ToDoubleFunction;
import java.util.stream.*;

public class Network {

    private List<List<Double>> examples;
    private List<Layer> layers;

    private int hiddenLayers;
    private int inputCount;
    private int dimension;
    private boolean isRadialBasis;

    public Network(int hiddenLayers, int inputCount, int dimension, boolean isRadialBasis) {
        this.hiddenLayers = hiddenLayers;
        this.inputCount = inputCount;
        this.dimension = dimension;
        this.isRadialBasis = isRadialBasis;
    }

    public void forwardPropogate() {}

    public void backPropogate(){}
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

}
