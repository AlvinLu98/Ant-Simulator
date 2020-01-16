import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;

public class Menu_Controller {
    @FXML
    private TextField antNoTextField;

    @FXML
    private TextField evaporationBox;

    @FXML
    private TextField foodAmtBox;

    @FXML
    private ComboBox<String> obstacleType;

    @FXML
    private TextField birthRateBox, lifespanBox;

    @FXML
    private TextField timeScaleBox;

    @FXML
    private Slider speedUp;

    @FXML
    Text time;

    @FXML
    Text scaledTime;

    @FXML
    Text antPop;

    @FXML
    Text antDeath;

    @FXML
    Text status;

    @FXML
    TableView pheromones;

    private boolean stopped = false, paused = false, play = false;
    Instant pauseStartTime;
    long totalPauseTime;
    boolean ready = false;



    /**
     * //https://stackoverflow.com/questions/32806068/how-to-change-fxml-lable-text-by-id
     * Initialises the controller
     */
    @FXML
    private void initialize(){
        //https://asgteach.com/2011/10/javafx-animation-and-binding-simple-countdown-timer-2/
        final Duration oneFrameAmt = Duration.millis(1000/60);
        final KeyFrame oneFrame = new KeyFrame(oneFrameAmt,
                event -> {
                    Ant_Simulator ant = (Ant_Simulator)Display.sim;
                    if(!paused) {
                        time.setText(String.valueOf(java.time.Duration.between(Simulator.start, Simulator.current).toMillis() / 1000));
                        java.time.Duration diff = java.time.Duration.between(Simulator.start, Simulator.scaledCurrent);
                        scaledTime.setText(diff.toDays() + " days " + (diff.toHours()) % 24 + " Hours " + (diff.toMinutes()) % 60 +
                                " Minutes " + (diff.toMillis() / 1000) % 60 + " Seconds");
                        antPop.setText(String.valueOf(Ant_Simulator.getAntPop()) + " ants alive");
                        antDeath.setText(String.valueOf(Ant_Simulator.getDeadAntPop() + " ants died"));
                    }

                    if(Ant_Simulator.getSelectedX() != -1){
                        pheromones.getItems().clear();
                        ArrayList<Pheromone_Data> pd = Ant_Simulator.getSurrounding(Ant_Simulator.getSelectedX(),
                                Ant_Simulator.getSelectedY()).getInList();
                        for(Ground_Data p: pd.get(4).getPheromones()){
                            if(p instanceof Pheromone){
                                Pheromone phe = (Pheromone)p;
                                pheromones.getItems().add(phe);
                            }
                        }
                    }

                    if(play){
                        status.setText("Running");
                    }
                    else if(paused){
                        status.setText("Paused");
                    }
                    else if(stopped){
                        status.setText("Stopped");
                    }
                });
        Display.timeline = new Timeline();
        Display.timeline.setCycleCount(Animation.INDEFINITE);
        Display.timeline.setAutoReverse(true);
        Display.timeline.getKeyFrames().add(oneFrame);
        play = true;
        paused = false;
        stopped = false;
        totalPauseTime = 0;

        Ant_Simulator ant = (Ant_Simulator)Display.sim;
        antNoTextField.setText(String.valueOf(ant.getInitAntAmt()));
        evaporationBox.setText(String.valueOf(1 - ant.getEvaporationRate()));
        foodAmtBox.setText(String.valueOf(ant.getNumFood()));
        obstacleType.setValue(String.valueOf(ant.getObstacleType()));
        birthRateBox.setText(String.valueOf(Ant_Simulator.getBirthRate()));
        lifespanBox.setText(String.valueOf(Ant_Simulator.getLifespan()));
        timeScaleBox.setText(String.valueOf(Ant_Simulator.getScale()));

        speedUp.valueProperty().addListener((observable, oldValue, newValue) -> {
            Simulator.timeline.setRate(newValue.doubleValue());
        });
    }

    /**
     * Starts the timeline
     */
    public static void start(){
        Display.timeline.play();
    }

    /**
     * PLays the simulation if paused and restarts the simulation if stopped
     */
    public void play(){
        if(stopped){
            resetSettings();
            if(ready) {
                stopped = false;
                initialize();
                Simulator.start = Instant.now();
                Display.sim.beginSimulation();
                play = true;
            }
        }
        if(paused){
            totalPauseTime = java.time.Duration.between(pauseStartTime, Instant.now()).toMillis();
            Ant_Simulator ant = (Ant_Simulator)Display.sim;
            Simulator.current = Simulator.current.plusMillis(-totalPauseTime);
            Simulator.scaledCurrent = Simulator.scaledCurrent.plusMillis(Ant_Simulator.getScale() * -totalPauseTime);
            ((Ant_Simulator)Display.sim).playSimulation();
            paused = false;
            play = true;
        }
    }

    /**
     * Pauses the simulation
     */
    public void pause(){
        paused = true;
        if(play) {
            play = false;
            pauseStartTime = Instant.now();
            Simulator.timeline.pause();
            Simulator.timer.stop();
        }
    }

    /**
     * Stops the simulation
     */
    public void stop(){
        paused = false;
        play = false;
        stopped = true;
        Simulator.timeline.stop();
        Simulator.timer.stop();
    }

    /**
     * Resets the settings of the simulator to the values entered in the controller
     */
    public void resetSettings(){
        boolean valid = true;
        Alert a = new Alert(Alert.AlertType.NONE);
        String errorMes = "";
        Ant_Simulator ant = (Ant_Simulator)Display.sim;

        if (!antNoTextField.getText().isEmpty() && Display.isDigit(antNoTextField.getText())) {
            ant.setInitAntAmt(Integer.parseInt(antNoTextField.getText()));
            Ant_Simulator.setAntPop(0);
        } else {
            a.setAlertType(Alert.AlertType.ERROR);
            errorMes += "Please enter a valid number for ants\n";
            antNoTextField.clear();
            valid = false;
        }

        if (!evaporationBox.getText().isEmpty() && Display.isDouble(evaporationBox.getText())) {
            double eva = 1 - Double.valueOf(evaporationBox.getText());
            if (eva < 1) {
                ant.setEvaporationRate(eva);
            }
        } else {
            a.setAlertType(Alert.AlertType.ERROR);
            errorMes += "Please enter a valid number for evaporation constant" +
                    " (number must be smaller than 1)\n";
            evaporationBox.clear();
            valid = false;
        }

        if (!foodAmtBox.getText().isEmpty() && Display.isDigit(foodAmtBox.getText())) {
            int amt = Integer.valueOf(foodAmtBox.getText());
            if (amt <= 10 && amt >= 0) {
                ant.setNumFood(amt);
            } else {
                a.setAlertType(Alert.AlertType.ERROR);
                errorMes += "Please enter a number between 0 - 10 for food!\n";
                foodAmtBox.clear();
                valid = false;
            }
        } else {
            a.setAlertType(Alert.AlertType.ERROR);
            errorMes += "Please enter a valid number for food\n";
            foodAmtBox.clear();
            valid = false;
        }

        if (obstacleType.getValue() != null) {
            ant.setObstacleType(String.valueOf(obstacleType.getValue()));
        } else {
            a.setAlertType(Alert.AlertType.ERROR);
            errorMes += "Select an Obstacle type\n";
            valid = false;
        }

        if (!birthRateBox.getText().isEmpty() && Display.isDigit(birthRateBox.getText())) {
            Ant_Simulator.setBirthRate(Integer.valueOf(birthRateBox.getText()));
        } else {
            a.setAlertType(Alert.AlertType.ERROR);
            errorMes += "Birth rate should be a number!\n";
            valid = false;
        }

        if (!lifespanBox.getText().isEmpty() && Display.isDigit(lifespanBox.getText())) {
            Ant_Simulator.setLifespan(Integer.valueOf(lifespanBox.getText()));
        } else {
            a.setAlertType(Alert.AlertType.ERROR);
            errorMes += "Lifespan should be a number!\n";
            valid = false;
        }

        if (!timeScaleBox.getText().isEmpty() && Display.isDigit(timeScaleBox.getText())) {
            Ant_Simulator.setScale(Integer.valueOf(timeScaleBox.getText()));
        } else {
            a.setAlertType(Alert.AlertType.ERROR);
            errorMes += "Time scale should be a number!\n";
            valid = false;
        }

        if (valid) {
            Display.timeline.stop();
            ((Ant_Simulator)Display.sim).reset(Display.primaryStage);
            Display.menuStage.close();
            setUpMap();

            Display.sim.buildLoop();

            Display.primaryStage.show();
            Display.menuStage.show();

            ((Ant_Simulator)Display.sim).setUp();
            ready = true;
        } else {
            a.setContentText(errorMes);
            a.show();
        }
    }

    /**
     * Creates the map creation prompter
     */
    public void setUpMap(){
        Rectangle2D screenSize = Screen.getPrimary().getBounds();

        Display.menuStage = new Stage();
        Display.menuStage.setTitle("Set up");
        Display.menuStage.setX(screenSize.getMinX() + screenSize.getMaxX() - 700);
        Display.menuStage.setY(screenSize.getMinY() + 20);

        Scene menuScene;
        try {
            Parent menu = FXMLLoader.load(getClass().getResource("Map_Creation.fxml"));
            menuScene = new Scene(menu, 500, 500);
            Display.menuStage.setOnCloseRequest(event -> {
                Platform.exit();
            });

            Display.menuStage.setResizable(false);
            Display.menuStage.setScene(menuScene);
        }catch (IOException e){
            System.out.println("Failed to load set up fxml file");
        }
    }
}
