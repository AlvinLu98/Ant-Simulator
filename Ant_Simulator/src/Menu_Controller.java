import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class Menu_Controller {
    @FXML
    Text time;
    private boolean stopped = false, paused = false, play = false;
    long pauseStartTime;
    long totalPauseTime;

    //https://stackoverflow.com/questions/32806068/how-to-change-fxml-lable-text-by-id
    @FXML
    private void initialize(){
        //https://asgteach.com/2011/10/javafx-animation-and-binding-simple-countdown-timer-2/
        final Duration oneFrameAmt = Duration.millis(1000/60);
        final KeyFrame oneFrame = new KeyFrame(oneFrameAmt,
                event -> {
                    time.setText(String.valueOf(Display.currentTime));
                });
        Display.timeline = new Timeline();
        Display.timeline.setCycleCount(Animation.INDEFINITE);
        Display.timeline.setAutoReverse(true);
        Display.timeline.getKeyFrames().add(oneFrame);
        pauseStartTime = 0;
        totalPauseTime = 0;
        play = true;

        Display.timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                Display.currentTime = (System.currentTimeMillis() - Display.startTime - totalPauseTime)/1000;
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
            Display.startTime = System.currentTimeMillis();
            Display.timeline.play();
            Display.sim.beginSimulation();
        }
        if(paused){
            totalPauseTime += System.currentTimeMillis() - pauseStartTime;
            Display.timeline.play();
            Display.sim.beginSimulation();
            paused = false;
        }
        play = true;
    }

    public void pause(){
        paused = true;
        if(play) {
            pauseStartTime = System.currentTimeMillis();
        }
        Display.timeline.pause();
        Ant_Simulator.timeline.pause();
        Ant_Simulator.timer.stop();
    }

    public void stop(){
        stopped = true;
        Display.timeline.stop();
        Ant_Simulator.timeline.stop();
        Ant_Simulator.timer.stop();
        ((Ant_Simulator)Display.sim).reset(Display.primaryStage);
    }
}
