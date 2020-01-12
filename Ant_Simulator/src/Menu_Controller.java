import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.time.Instant;

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

    private boolean stopped = false, paused = false, play = false;
    Instant pauseStartTime;
    long totalPauseTime;

    //https://stackoverflow.com/questions/32806068/how-to-change-fxml-lable-text-by-id
    @FXML
    private void initialize(){
        //https://asgteach.com/2011/10/javafx-animation-and-binding-simple-countdown-timer-2/
        final Duration oneFrameAmt = Duration.millis(1000/60);
        final KeyFrame oneFrame = new KeyFrame(oneFrameAmt,
                event -> {
                    Ant_Simulator ant = (Ant_Simulator)Display.sim;
                    time.setText(String.valueOf(java.time.Duration.between(Simulator.start, Simulator.current).toMillis()/1000));
                    java.time.Duration diff = java.time.Duration.between(Simulator.start, Simulator.scaledCurrent);
                    scaledTime.setText(diff.toDays() + " days " + (diff.toHours())%24 + " Hours " + (diff.toMinutes())%60 +
                            " Minutes " + (diff.toMillis()/1000)%60 + " Seconds");
                    antPop.setText(String.valueOf(Ant_Simulator.getAntPop()) + " ants alive");
                });
        Display.timeline = new Timeline();
        Display.timeline.setCycleCount(Animation.INDEFINITE);
        Display.timeline.setAutoReverse(true);
        Display.timeline.getKeyFrames().add(oneFrame);
        play = true;
        totalPauseTime = 0;

        Ant_Simulator ant = (Ant_Simulator)Display.sim;
        antNoTextField.setText(String.valueOf(ant.getInitAntAmt()));
        evaporationBox.setText(String.valueOf(ant.getEvaporationRate()));
        foodAmtBox.setText(String.valueOf(ant.getNumFood()));
        obstacleType.setValue(String.valueOf(ant.getObstacleType()));
        birthRateBox.setText(String.valueOf(ant.getBirthRate()));
        lifespanBox.setText(String.valueOf(ant.getLifespan()));
        timeScaleBox.setText(String.valueOf(ant.getScale()));

        speedUp.valueProperty().addListener((observable, oldValue, newValue) -> {
            Display.sim.timeline.setRate(newValue.doubleValue());
        });
    }

    public static void start(){
        Display.timeline.play();
    }

    public void play(){
        if(stopped){
            initialize();
            stopped = false;
            resetSettings();
//            Simulator.start = Instant.now();
//            Display.timeline.play();
//            Display.sim.beginSimulation();
        }
        if(paused){
            totalPauseTime = java.time.Duration.between(pauseStartTime, Instant.now()).toMillis();
            Ant_Simulator ant = (Ant_Simulator)Display.sim;
            Simulator.current = Simulator.current.plusMillis(-totalPauseTime);
            Simulator.scaledCurrent = Simulator.scaledCurrent.plusMillis(ant.getScale() * -totalPauseTime);
            Display.timeline.play();
            ((Ant_Simulator)Display.sim).playSimulation();
            paused = false;
        }
        play = true;
    }

    public void pause(){
        paused = true;
        if(play) {
            play = false;
            pauseStartTime = Instant.now();
            Display.timeline.pause();
            Ant_Simulator.timeline.pause();
            Ant_Simulator.timer.stop();
        }
    }

    public void stop(){
        paused = false;
        stopped = true;
        Display.timeline.stop();
        Ant_Simulator.timeline.stop();
        Ant_Simulator.timer.stop();
    }

    public void resetSettings(){
        boolean valid = true;
        boolean ready = false;
        Alert a = new Alert(Alert.AlertType.NONE);
        String errorMes = "";
        Ant_Simulator ant = (Ant_Simulator)Display.sim;

        while(!ready) {
            if (!antNoTextField.getText().isEmpty() && Display.isDigit(antNoTextField.getText())) {
                ant.setInitAntAmt(Integer.parseInt(antNoTextField.getText()));
            } else {
                a.setAlertType(Alert.AlertType.ERROR);
                errorMes += "Please enter a valid number for ants\n";
                antNoTextField.clear();
                valid = false;
            }

            if (!evaporationBox.getText().isEmpty() && Display.isDouble(evaporationBox.getText())) {
                double eva = Double.valueOf(evaporationBox.getText());
                System.out.println(eva);
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
                ant.setBirthRate(Integer.valueOf(birthRateBox.getText()));
            } else {
                a.setAlertType(Alert.AlertType.ERROR);
                errorMes += "Birth rate should be a number!\n";
                valid = false;
            }

            if (!lifespanBox.getText().isEmpty() && Display.isDigit(lifespanBox.getText())) {
                ant.setLifespan(Integer.valueOf(lifespanBox.getText()));
            } else {
                a.setAlertType(Alert.AlertType.ERROR);
                errorMes += "Lifespan should be a number!\n";
                valid = false;
            }

            if (!timeScaleBox.getText().isEmpty() && Display.isDigit(timeScaleBox.getText())) {
                ant.setScale(Integer.valueOf(timeScaleBox.getText()));
            } else {
                a.setAlertType(Alert.AlertType.ERROR);
                errorMes += "Time scale should be a number!\n";
                valid = false;
            }

            if (valid) {
                ((Ant_Simulator)Display.sim).reset(Display.primaryStage);
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
    }

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
