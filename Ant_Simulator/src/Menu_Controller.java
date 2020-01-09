import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.time.Instant;

public class Menu_Controller {
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
                    time.setText(String.valueOf(java.time.Duration.between(Simulator.start, Display.current).toMillis()/1000));
                    scaledTime.setText(java.time.Duration.between(ant.start, ant.current).toDays() + " days");
                    antPop.setText(String.valueOf(Ant_Simulator.getAntPop()));
                });
        Display.timeline = new Timeline();
        Display.timeline.setCycleCount(Animation.INDEFINITE);
        Display.timeline.setAutoReverse(true);
        Display.timeline.getKeyFrames().add(oneFrame);
        play = true;
        totalPauseTime = 0;

        Display.timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                Display.current = Instant.now().plusMillis(totalPauseTime);
            }
        };
    }

    public static void start(){
        Display.timeline.play();
        Display.timer.start();
    }

    public void play(){
        if(stopped){
            initialize();
            stopped = false;
            Simulator.start = Instant.now();
            Display.timeline.play();
            Display.sim.beginSimulation();
        }
        if(paused){
            totalPauseTime += java.time.Duration.between(Instant.now(), pauseStartTime).toMillis();
            Display.timeline.play();
            Display.sim.beginSimulation();
            paused = false;
        }
        play = true;
    }

    public void pause(){
        paused = true;
        if(play) {
            pauseStartTime = Instant.now();
        }
        Display.timeline.pause();
        Ant_Simulator.timeline.pause();
        Ant_Simulator.timer.stop();
    }

    public void stop(){
        paused = false;
        stopped = true;
        Display.timeline.stop();
        Ant_Simulator.timeline.stop();
        Ant_Simulator.timer.stop();
        ((Ant_Simulator)Display.sim).reset(Display.primaryStage);
    }
}
