import java.util.List;

public class Network {

    private List<List<Double>> examples;
    private List<Layer> layers;

    public Network(int hiddenLayers, int nodesPerHiddenLayer, int inputCount, int dimension, boolean isRadialBasis) {}

    public void forwardPropogate(){}
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
        double sum = 0;
        for(int i = 0; i < values.length-2; i++) {
            sum += Math.pow(Math.pow(1-values[i], 2) + 100 * (values[i+1] - Math.pow(values[i], 2)), 2);
        }
        return sum;
    }

}
