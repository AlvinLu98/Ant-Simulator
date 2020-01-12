import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.Instant;

public class Display extends Application {
    public static Timeline timeline;
    public static Instant current = Instant.now();
    public static Stage menuStage;
    public static Stage primaryStage;
    public static long prevTime = 0;

    public static Simulator sim = new Ant_Simulator(60); //Creates an ant simulator
    private Parent root;
    private Scene scene;

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
            this.root = FXMLLoader.load(getClass().getResource("Initial_Form.fxml"));
            Initial_Form_Controller.setStage(primaryStage);
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
