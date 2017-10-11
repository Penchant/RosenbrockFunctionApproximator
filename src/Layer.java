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
        IntStream.range(0, nodeCount)
                .boxed()
                .parallel()
                .forEach(i -> getNodeArray()[i] = new Node(layerType));
    }

    public void updateNodeWeights(List<List<Double>> weights) {

        IntStream.range(0, weights.size())
                .boxed()
                .parallel()
                .forEach(i -> getNodeArray()[i].updateWeights(weights.get(i)));
    }

    public List<Double> calculateNodeOutputs(){

        return Stream.of(getNodeArray())
                .parallel()
                .map(i -> {
                    i.calculateOutput();
                    return i.output;
                    }
                )
                .collect(Collectors.toList());
    }

    public Node[] getNodeArray()
    {
        return nodes.toArray(new Node[nodes.size()]);
    }

}
