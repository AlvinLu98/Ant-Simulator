import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class MenuController {
    @FXML
    Label time;

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

        Display.timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                Display.currentTime = System.currentTimeMillis() - Display.startTime;
            }
        };
    }
}
