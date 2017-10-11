import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.ArrayList;

public class Layer {

    private Type layerType;
    public List<Node> nodes;
    public int layerCount;

    public Layer(int nodeCount, Type layerType) {
        nodes = new ArrayList<Node>();

        Node[] nodeArray = nodes.toArray(new Node[nodes.size()]);
        IntStream.range(0, nodeCount)
                .boxed()
                .parallel()
                .forEach(i -> nodeArray[i] = new Node(layerType));
    }

    public void updateNodeWeights(List<List<Double>> weights) {
        Node[] nodeArray = nodes.toArray(new Node[nodes.size()]);
        IntStream.range(0, weights.size())
                .boxed()
                .parallel()
                .forEach(i -> nodeArray[i].updateWeights(weights.get(i)));
    }

    public List<Double> calculateNodeOutputs(){

        Node[] nodeArray = nodes.toArray(new Node[nodes.size()]);
        return Stream.of(nodeArray)
                .parallel()
                .map(n -> n.calculateOutput())
                .collect(Collectors.toList());
    }

}
