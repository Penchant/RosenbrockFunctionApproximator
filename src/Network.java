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
    private double rosenbrock(double ... values){return 0d;}

}
