import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * An abstract class defining the base of a simulator
 * This class initialises a window and updates it using frame updates
 * author Alvin Lu
 * code adapted from https://dzone.com/articles/javafx-2-game-tutorial-part-2
 */
public abstract class Simulator{

    /** Sets the size of the window */
    public static final int WIDTH = 640;
    public static final int HEIGHT = WIDTH/12*9;

    /** JavaFX Scene */
    protected Scene scene;

    /** Contains all nodes to be displayed on screen */
    protected Group root;

    /** loop for simulator */
    protected static Timeline timeline;

    /** Handler for all simulation objects */
    protected Handler handler;

    /** frames per second */
    protected final int fps;

    /**
     * Default constructor for Simulator
     */
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
     * Creates a timeline object that allows update of individual objects
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

    /**
     * Checks for collision for each individual sprite in the simulator
     */
    public void checkCollision(){
        this.handler.resetCollisionCheck();

        for(Sprite s: this.handler.getObjects()){
            for(Sprite c: this.handler.getCollisionCheck()){
                if(s != c){
                    handleCollision(s,c);
                }
            }
        }
    }

    /**
     * Base method for handling collisions
     * returns false by default
     * @param s current sprite
     * @param c sprite to check collision against
     * @return true if objects collide
     */
    public boolean handleCollision(Sprite s, Sprite c){
        return false;
    }

    /**
     * Gets the scene object
     * @return javafx.scene.Scene
     */
    public Scene getScene() {
        return scene;
    }

    /**
     * Sets the scene object
     * @param scene
     */
    public void setScene(Scene scene) {
        this.scene = scene;
    }

    /**
     * Gets the group object
     * @return javafx.scene.Group
     */
    public Group getGroup() {
        return root;
    }

    /**
     * Sets the root object
     * @param root
     */
    public void setGroup(Group root) {
        this.root = root;
    }

    /**
     * Get the timeline object
     * @return javafx.animation.timeline
     */
    public static Timeline getTimeline() {
        return timeline;
    }

    /**
     * Sets the timeline
     * @param timeline
     */
    public static void setTimeline(Timeline timeline) {
        Simulator.timeline = timeline;
    }

    /**
     * Get the handler object
     * @return Handler
     */
    public Handler getHandler() {
        return handler;
    }

    /**
     * Sets the handler
     * @param handler
     */
    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    /**
     * Get the frames per second
     * @return int
     */
    public int getFps() {
        return fps;
    }

}
