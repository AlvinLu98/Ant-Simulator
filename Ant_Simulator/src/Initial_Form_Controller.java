import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * https://www.callicoder.com/javafx-fxml-form-gui-tutorial/
 */
public class Initial_Form_Controller {
    @FXML
    private TextField antNoTextField;

    @FXML
    private TextField evaporationBox;

    @FXML
    private TextField foodAmtBox;

    @FXML
    private ComboBox<String> obstacleType;

    private static boolean ready = false;

    public static void setStage(Stage stage){
        Display.primaryStage = stage;
    }

    @FXML
    public void setUpStartButton(){
        boolean valid = true;
        Alert a = new Alert(Alert.AlertType.NONE);
        String errorMes = "";
        Ant_Simulator ant = (Ant_Simulator)Display.sim;
        if(!antNoTextField.getText().isEmpty() && Display.isDigit(antNoTextField.getText())){
            ant.setNumAnt(Integer.parseInt(antNoTextField.getText()));
        }
        else{
            a.setAlertType(Alert.AlertType.ERROR);
            errorMes += "Please enter a valid number for ants\n";
            antNoTextField.clear();
            valid = false;
        }

        if(!evaporationBox.getText().isEmpty() && Display.isDouble(evaporationBox.getText())){
            double eva = Double.valueOf(evaporationBox.getText());
            System.out.println(eva);
            if(eva < 1) {
                ant.setEvaporationRate(eva);
            }
        }
        else{
            a.setAlertType(Alert.AlertType.ERROR);
            errorMes +="Please enter a valid number for evaporation constant" +
                    " (number must be smaller than 1)\n";
            evaporationBox.clear();
            valid = false;
        }

        if(!foodAmtBox.getText().isEmpty() && Display.isDigit(foodAmtBox.getText())){
            int amt = Integer.valueOf(foodAmtBox.getText());
            if(amt <= 10 && amt >= 0) {
                ant.setNumFood(amt);
            }
            else{
                a.setAlertType(Alert.AlertType.ERROR);
                errorMes += "Please enter a number between 0 - 10 for food!\n";
                foodAmtBox.clear();
                valid = false;
            }
        }
        else{
            a.setAlertType(Alert.AlertType.ERROR);
            errorMes += "Please enter a valid number for food\n";
            foodAmtBox.clear();
            valid = false;
        }

        if(obstacleType.getValue() != null){
            ant.setObstacleType(String.valueOf(obstacleType.getValue()));
        }
        else{
            a.setAlertType(Alert.AlertType.ERROR);
            errorMes += "Select an Obstacle type";
            valid = false;
        }

        if(valid) {
            setUpSimulation(Display.primaryStage);
        }
        else{
            a.setContentText(errorMes);
            a.show();
        }
    }

    public static boolean ready(){
        return ready;
    }

    private void setUpSimulation(Stage primaryStage){
        Display.sim.initialize(primaryStage);
        setUpMenu();

        Display.sim.buildLoop();

        primaryStage.show();
        Display.menuStage.show();

        ((Ant_Simulator)Display.sim).setUp();
    }

    private void setUpMenu(){
        Display.startTime = System.currentTimeMillis();
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
    }
}
