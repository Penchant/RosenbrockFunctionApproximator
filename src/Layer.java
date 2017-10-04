import java.util.List;

public class Layer {

    private Type layerType;
    public List<Node> nodes;
    public int layerCount;

    public Layer(int nodes, Type layerType) {}

    public void updateNodeWeights(List<List<Double>> weights){};
    public List<Double> calculateNodeOutputs(){return null;};

}
