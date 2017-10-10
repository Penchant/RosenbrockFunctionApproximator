import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.File;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(FXMLLoader.load(new File("res/format.fxml").toURI().toURL()), 640, 480));
        primaryStage.show();
    }

    public static void start (int dataGenStart, int dataGenEnd, int dataGenIncrement, int hiddenLayers, int inputCount, int nodesPerHiddenLater, boolean isRadialBasis) {
        Network network = new Network(hiddenLayers, nodesPerHiddenLater, inputCount, isRadialBasis);

    }

    public static void main(String[] args) {
        launch(args);
    }

}
