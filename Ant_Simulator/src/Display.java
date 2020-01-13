import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
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
    public void initialize(Stage menuStage){
        createAntForm(menuStage);
    }

    private void createAntForm(Stage primaryStage){
        Ant_Simulator ant = (Ant_Simulator)Display.sim;
        ant.setInitAntAmt(500);
        ant.setEvaporationRate(0.999);
        ant.setNumFood(1);
        ant.setObstacleType("Two Obstacle");
        Ant_Simulator.setBirthRate(10);
        Ant_Simulator.setLifespan(1);
        Ant_Simulator.setScale(1);
        setUpSimulation(primaryStage);
    }

    private void setUpSimulation(Stage primaryStage){
        sim.initialize(primaryStage);
        setUpMenu();

        sim.buildLoop();

        primaryStage.show();
        menuStage.show();

        ((Ant_Simulator)sim).setUp();
    }

    private void setUpMenu(){
        //https://coderanch.com/t/620036/java/Stage-corner-screen
        Rectangle2D screenSize = Screen.getPrimary().getBounds();

        Display.menuStage = new Stage();
        Display.menuStage.setTitle("Set up");
        Display.menuStage.setX(screenSize.getMinX() + screenSize.getMaxX() - 700);
        Display.menuStage.setY(screenSize.getMinY() + 20);

        Scene menuScene;

        try {
            Parent menu = FXMLLoader.load(getClass().getResource("Map_Creation.fxml"));
            menuScene = new Scene(menu, 500, 500);
            createMenuEvents();
            Display.menuStage.setScene(menuScene);
        }catch (IOException e){
            System.out.println("Failed to load set up fxml file");
        }
    }

    private void createMenuEvents(){
        //https://www.programcreek.com/java-api-examples/?class=javafx.stage.Stage&method=setOnCloseRequest
        Display.menuStage.setOnCloseRequest(event -> {
            Platform.exit();
        });

        Display.menuStage.setResizable(false);
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
