import javafx.application.Application;
import javafx.stage.Stage;

public class Display extends Application {
    Simulator sim = new AntSimulator(60); //Creates an ant simulator

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setResizable(false); //Set Window size to be not resizeable

        sim.initialize(primaryStage); //Initialise the stage

        sim.beginSimulation(); //begins the loop

        primaryStage.show(); //display the Stage
    }
}
