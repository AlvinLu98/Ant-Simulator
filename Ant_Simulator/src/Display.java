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

    /**
     * Creates and shows the stage
     * @param primaryStage
     */
    @Override
    public void start(Stage primaryStage){
        primaryStage.setResizable(false); //Set Window size to be not resizeable
        Display.primaryStage = primaryStage;
        initialize(primaryStage);
        primaryStage.show();
    }

    /**
     * Initialises the stage
     * //https://docs.oracle.com/javafx/2/get_started/form.htm
     * @param primaryStage
     */
    public void initialize(Stage primaryStage){
        defaultSettings(primaryStage);
    }

    /**
     * Creates an ant simulator with default settings
     * @param primaryStage
     */
    private void defaultSettings(Stage primaryStage){
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

    /**
     * Set up the simulation
     * @param primaryStage
     */
    private void setUpSimulation(Stage primaryStage){
        sim.initialize(primaryStage);
        setUpMenu();

        sim.buildLoop();

        primaryStage.show();
        menuStage.show();

        ((Ant_Simulator)sim).setUp();
    }

    /**
     * Creates the set up menu for map creation
     */
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
            menuScene = new Scene(menu, 500, 500/12*9);
            createMenuEvents();
            Display.menuStage.setX(screenSize.getMinX() + screenSize.getMaxX() - 650);
            Display.menuStage.setY(screenSize.getMinY() + 50);
            Display.menuStage.setScene(menuScene);
        }catch (IOException e){
            System.out.println("Failed to load set up fxml file");
        }
    }

    /**
     * Creates the events on the menus
     * //https://www.programcreek.com/java-api-examples/?class=javafx.stage.Stage&method=setOnCloseRequest
     */
    private void createMenuEvents(){
        Display.menuStage.setOnCloseRequest(event -> {
            Platform.exit();
        });

        Display.menuStage.setResizable(false);
    }

    /**
     * Check if a given string is a digit
     * //https://www.geeksforgeeks.org/program-check-input-integer-string/
     * @param s String to be checked
     * @return true if the string is a digit
     */
    public static boolean isDigit(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (!Character.isDigit(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check if a given string is a double
     * //https://www.geeksforgeeks.org/program-check-input-integer-string/
     * @param s String to be checked
     * @return true if the string is a double
     */
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
