import java.util.List;

public class Network {

    private List<List<Double>> examples;
    private List<Layer> layers;

    public Network(int hiddenLayers, int nodesPerHiddenLayer, int inputCount, int dimension, boolean isRadialBases) {}

    public void forwardPropogate(){}
    public void backPropogate(){}
    public List<Double> calculateError(){return null;}
    private void kMeansCluster(int k){}
    private double calculateSigma(){return 0d;}

    private double rossenBrock(double ... values) {
        double sum = 0;
        for(int i = 0; i < values.length-1; i++) {
            sum += Math.pow(Math.pow(1-values[i], 2) + 100 * (values[i+1] - Math.pow(values[i], 2)), 2);
        }
        return sum;
    }

}
