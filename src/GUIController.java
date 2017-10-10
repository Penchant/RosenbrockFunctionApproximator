import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class GUIController {

    @FXML
    Button selectFileButton;

    @FXML
    Button startButton;

    @FXML
    TextField dataGenStartTextField;

    @FXML
    TextField dataGenEndTextField;

    @FXML
    TextField dataGenIncrementTextField;

    @FXML
    TextField hiddenLayersTextField;

    @FXML
    TextField inputCountTextField;

    @FXML
    TextField nodesPerHiddenLayerTextField;

    @FXML
    CheckBox isRadialBasisCheckbox;

    @FXML
    ProgressBar progressBar;

    @FXML
    private void selectFile(MouseEvent event) {
        System.out.println(selectFileButton.getText());
    }

    @FXML
    private void start(MouseEvent event) {
        int dataGenStart = Integer.parseInt(dataGenStartTextField.getText());
        int dataGenEnd = Integer.parseInt(dataGenEndTextField.getText());
        int dataGenIncrement = Integer.parseInt(dataGenIncrementTextField.getText());
        int hiddenLayers = Integer.parseInt(hiddenLayersTextField.getText());
        int inputCount = Integer.parseInt(inputCountTextField.getText());
        int nodesPerHiddenLater = Integer.parseInt(nodesPerHiddenLayerTextField.getText());
        boolean isRadialBasis = isRadialBasisCheckbox.isSelected();

        Main.start(dataGenStart, dataGenEnd, dataGenIncrement, hiddenLayers, inputCount, nodesPerHiddenLater, isRadialBasis);
    }

}
