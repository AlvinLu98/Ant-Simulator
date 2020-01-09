import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class Map_Creation_Controller {
    @FXML
    Text instruction;

    @FXML
    private void initialize(){
        final Duration oneFrameAmt = Duration.millis(1000/60);
        final KeyFrame oneFrame = new KeyFrame(oneFrameAmt,
                event -> {
                    if(!Ant_Simulator.setUpHive){
                        instruction.setText("Please select home location");
                    }
                    else if(!Ant_Simulator.setUpFood){
                        instruction.setText("Please select food location");
                    }
                });
        Display.timeline = new Timeline();
        Display.timeline.setCycleCount(Animation.INDEFINITE);
        Display.timeline.setAutoReverse(true);
        Display.timeline.getKeyFrames().add(oneFrame);
        Display.timeline.play();
    }

    public Text getInstruction() {
        return instruction;
    }

    public void setInstruction(Text instruction) {
        this.instruction = instruction;
    }
}
