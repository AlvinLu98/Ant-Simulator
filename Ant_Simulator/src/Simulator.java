import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

public abstract class Simulator{

    /** Sets the size of the window */
    public static final int WIDTH = 640;
    public static final int HEIGHT = WIDTH/12*9;

    /** JavaFX Scene */
    protected Scene scene;

    /** All nodes to be displayed on screen */
    protected Group root;

    /** loop for simulator */
    protected static Timeline timeline;

    /** Handler for all simulation objects */
    protected Handler handler;

    protected final int fps;

    public Simulator(){
        this.fps = 60;
        buildLoop();
    }

    public Simulator(final int fps) {
        this.fps = fps;
        this.handler = new Handler();
        buildLoop();
    }

    /**
     * Builds the game loop
     */
    protected final void buildLoop(){

        final Duration oneFrameAmt = Duration.millis(1000/fps);
        final KeyFrame oneFrame = new KeyFrame(oneFrameAmt,
                new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent event) {
                        handler.tick();

                        checkCollision();
                    }
                });
        Simulator.timeline = new Timeline();
        Simulator.timeline.setCycleCount(Animation.INDEFINITE);
        Simulator.timeline.setAutoReverse(true);
        Simulator.timeline.getKeyFrames().add(oneFrame);
    }

    /**
     * Initialize game world by updating the JavaFX Stage
     * @param primaryStage
     */
   public abstract void initialize(final Stage primaryStage);


    /**
     * Begin the simulation
     */
    public void beginSimulation(){
        Simulator.timeline.play();
    }

    public void checkCollision(){
        this.handler.resetCollisionCheck();

        for(Sprite s: this.handler.getObjects()){
            for(Sprite c: this.handler.getCollisionCheck()){
                handleCollision(s,c);
            }
        }
    }

    public boolean handleCollision(Sprite s, Sprite c){
        return false;
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public Group getRoot() {
        return root;
    }

    public void setRoot(Group root) {
        this.root = root;
    }

    public static Timeline getTimeline() {
        return timeline;
    }

    public static void setTimeline(Timeline timeline) {
        Simulator.timeline = timeline;
    }

    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public int getFps() {
        return fps;
    }

}
