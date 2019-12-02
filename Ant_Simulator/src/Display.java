import javafx.animation.AnimationTimer;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class Display extends Application {
    public static AnimationTimer timer;
    public static Timeline timeline;
    public static long startTime = 0;
    public static long currentTime = 0;
    public static Label time;
    public static Text instruction = new Text("Setting up....");
    public static Text warning =  new Text("");

    public static Simulator sim = new AntSimulator(60); //Creates an ant simulator
    private Parent root;
    private Scene scene;
    private Stage menuStage;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage){
        primaryStage.setResizable(false); //Set Window size to be not resizeable
        initialize(primaryStage);
        primaryStage.show();
    }

    //https://docs.oracle.com/javafx/2/get_started/form.htm
    public void initialize(Stage formStage){
        formStage.setTitle("Ant info");

        createAntForm(formStage);

        this.scene = new Scene(root, Simulator.WIDTH, Simulator.HEIGHT);
        formStage.setScene(scene);
    }

    private void createAntForm(Stage primaryStage){
        try {
            this.root = FXMLLoader.load(getClass().getResource("SimulatorForm.fxml"));
            SimulatorFormController.setStage(primaryStage);
        }catch (IOException e){
            System.out.println("Failed to load fxml file");
        }
    }

    //https://www.geeksforgeeks.org/program-check-input-integer-string/
    public static boolean isDigit(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (!Character.isDigit(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isDouble(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (!Character.isDigit(s.charAt(i))) {
                if(Character.compare(s.charAt(i), '.') != 0) {
                    return false;
                }
            }
        }
        return true;
    }
}
