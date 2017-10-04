import java.util.List;

public class Node {

    private Type nodeType;
    public double output;
    public List<Double> inputs;
    public List<Double> weights;

    public double calculateOutputs(){return 0d;};
    public void updateWeights(List<Double> weightChanges){};

}
