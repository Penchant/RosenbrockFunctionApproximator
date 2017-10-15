import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Layer {

    public List<Node> nodes;
    public Type layerType;
    public int id;
    static public int count = 0;
    static public Network network;

    public Layer(int nodeCount, Type layerType) {
        this.layerType = layerType;
        this.id = count;
        count++;

        int inputCount = layerType == Type.INPUT ? nodeCount : network.layers.get(id -1).nodes.size();

        nodes = IntStream.range(0, nodeCount)
                .boxed()
                .parallel()
                .map(i -> new Node(layerType, inputCount))
                .collect(Collectors.toList());
    }

    public void updateNodeWeights() {
        nodes.stream()
                .parallel()
                .forEach(i -> i.updateWeights());
    }

    public List<Double> calculateNodeOutputs() {
        return nodes.stream()
                .parallel()
                .map(Node::calculateOutput)
                .collect(Collectors.toList());
    }

}
