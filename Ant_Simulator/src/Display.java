import javafx.application.Application;
import javafx.stage.Stage;

public class Display extends Application {
    Simulator sim = new AntSimulator(60);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setResizable(false);

        sim.initialize(primaryStage);

        sim.beginSimulation();

        primaryStage.show();
    }
}
