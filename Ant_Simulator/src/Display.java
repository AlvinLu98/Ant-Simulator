import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Display extends Application {
    private Simulator sim = new AntSimulator(60); //Creates an ant simulator
    private GridPane grid;
    private Scene scene;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage){
        primaryStage.setResizable(false); //Set Window size to be not resizeable

//        initialize(primaryStage);
//        primaryStage.show();

        sim.initialize(primaryStage); //Initialise the stage

        sim.beginSimulation(); //begins the loop

        primaryStage.show(); //display the Stage
    }

    public void initialize(Stage primaryStage){
        primaryStage.setTitle("Test");
        this.grid = new GridPane();

        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25,25,25,25));

        this.scene = new Scene(grid, Simulator.WIDTH, Simulator.HEIGHT);
        primaryStage.setScene(scene);
    }
}
