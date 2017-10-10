import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.*;
import javafx.stage.FileChooser.*;
import javafx.stage.Stage;

import java.io.File;
import java.util.function.Function;
import java.util.stream.Stream;

public class Main extends Application {

    private static Stage primaryStage;
    private static GUIController controller;

    private static boolean shouldStop = false;

    private static boolean useGUI = true;
    private static boolean isRadialBasis = false;
    private static int dataGenStart = 0;
    private static int dataGenEnd = 0;
    private static int dataGenIncrement = 0;
    private static int hiddenLayers = 0;
    private static int dimension = 0;
    private static int nodesPerHiddenLayer = 0;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("format.fxml"));
        primaryStage.setScene(new Scene(loader.load()));
        controller = loader.getController();
        primaryStage.titleProperty().set("Rosenbrock Function Approximator");
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("rosenbrock.jpg")));
        primaryStage.show();
        this.primaryStage = primaryStage;
    }

    @Override
    public void stop() throws Exception {
        shouldStop = true;
        super.stop();
    }

    private static javax.swing.Timer timer;
    private static double progress = 0;

    public static void start (double dataGenStart, double dataGenEnd, double dataGenIncrement, int hiddenLayers, int inputCount, int nodesPerHiddenLater, boolean isRadialBasis) {
        Network network = new Network(hiddenLayers, nodesPerHiddenLater, inputCount, isRadialBasis);

        // "Test" the progress bar
        if(useGUI) {
            timer = new javax.swing.Timer(1, ae -> {
                if((int)(Math.random() * 4) != 0 && progress <= 0.97d)
                    controller.progressBar.setProgress(progress += 0.0001d);
                if(shouldStop)
                    timer.stop();
            });

            timer.start();
        }
    }

    public static void save() {
        FileChooser fileChooser = new FileChooser();
        // TODO: Customise title
        fileChooser.setTitle("TODO");
        // TODO: Customise extensions
        fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("Text Files", "*.txt"),
                new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"),
                new ExtensionFilter("Audio Files", "*.wav", "*.mp3", "*.aac"),
                new ExtensionFilter("All Files", "*.*"));
        File selectedFile = fileChooser.showSaveDialog(primaryStage);
        // TODO: Save
    }

    private static CommandLineParameter[] commands = {
            new CommandLineParameter("-nogui",  "Runs the application without a GUI - default [False]",                 f -> useGUI = false,                false), // No GUI
            new CommandLineParameter("-h",      "Displays the help text",                                               f -> printHelp(),                   false), // Help
            new CommandLineParameter("-rb",     "Sets the network to use radial basis - default [False]",               f -> isRadialBasis = true,          false), // Radial Basis
            new CommandLineParameter("-ds",     "The start point for the data (example) generation - default [???]",    i -> dataGenStart = (int) i,        true),  // Data Generation Start
            new CommandLineParameter("-de",     "The end point for the data (example) generation - default [???]",      i -> dataGenEnd = (int) i,          true),  // Data Generation End
            new CommandLineParameter("-di",     "The incrementation of the data point - default [???]",                 i -> dataGenIncrement = (int) i,    true),  // Data Generation Incrementation
            new CommandLineParameter("-hl",     "The amount of hidden layers - default [???]",                          i -> hiddenLayers = (int) i,        true),  // Hidden Layers
            new CommandLineParameter("-d",      "The number of dimensions the function will use - default [2]",         i -> dimension = (int) i,           true),  // Dimensions
            new CommandLineParameter("-n",      "The number of nodes per hidden layer - default [???]",                 i -> nodesPerHiddenLayer = (int) i, true),  // Nodes Per Hidden Layer
            new CommandLineParameter("-s",      "Save the weights to a given output file             ",                 f -> saveCommand(),                 false), // Save
    };

    private static boolean printHelp() {
        Stream.of(commands).forEach(System.out::println);
        System.exit(0);
        return false;
    }

    private static boolean saveCommand() {
        save();
        return true;
    }

    public static void main(String[] args) {
        try {
            for (int i = 0; i < args.length; i++) {
                for (CommandLineParameter command : commands) {
                    if (args[i].equals(command.flag)) {
                        if (command.hasParam) {
                            i++;
                            command.func.apply(Integer.parseInt(args[i]));
                        } else {
                            command.func.apply(null);
                        }
                    }
                }
            }
        } catch (NumberFormatException nfe) {
            System.err.println("Invalid arguments");
            System.exit(1);
        }

        if(useGUI) {
            launch(args);
        } else {
            start(dataGenStart, dataGenEnd, dataGenIncrement, hiddenLayers, dimension, nodesPerHiddenLayer, isRadialBasis);
            System.exit(0);
        }

    }

    /**
     * Class that represents a command line parameter
     */
    private static class CommandLineParameter {

        public String flag;
        public String helpText;
        public Function func;
        public boolean hasParam;

        public CommandLineParameter(String flag, String helpText, Function func, boolean hasParam) {
            this.flag = flag;
            this.helpText = helpText;
            this.func = func;
            this.hasParam = hasParam;
        }

        @Override
        public String toString() {
            return flag + "\t" + helpText + (hasParam ? "" : "\t Takes no parameter");
        }

    }

}
