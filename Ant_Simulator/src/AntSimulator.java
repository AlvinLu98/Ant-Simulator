import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.util.Random;

public class AntSimulator extends Simulator{

    private int numAnt; //number of ants to create
    private int homeX, homeY; //location of hive

    /**
     * Default constructor for Ant simulator
     * @param fps frames per second
     */
    public AntSimulator(int fps){
        super(fps);
        numAnt = 500;
    }

    /**
     * Contructor that defines the number of ants
     * @param fps
     * @param ants
     */
    public AntSimulator(int fps, int ants){
        super(fps);
        this.numAnt = ants;
    }

    /**
     * Initialises the simulator
     * @param primaryStage
     */
    @Override
    public void initialize(final Stage primaryStage){
        primaryStage.setTitle("Ant simulator"); //Set the title of the window
        this.root = new Group(); //create a group to contain sprite objects
        this.scene = new Scene(this.root,WIDTH,HEIGHT, Color.LIGHTGREEN); //creates a scene to display the objects
        primaryStage.setScene(this.scene); //add the scene to the stage

        Random r = new Random();
        this.homeX = r.nextInt(WIDTH); //generate random x location for hive
        this.homeY = r.nextInt(HEIGHT); //generate random y location for the hive

        //Generates the item in the simulator
        generateHive();
        generateAnts(numAnt, this.homeX, this.homeY);
        generateFood();
    }

    /**
     * Overridden method
     * Checks ants to different sprites
     * @param s current sprite
     * @param c sprite to check collision against
     * @return
     */
    @Override
    public boolean handleCollision(Sprite s, Sprite c){
        if(s instanceof Ant){
            Ant a = (Ant)s;
            if(a.collision(c)){
                    ((Circle)s.getNode()).setRadius(3.0);
                    ((Circle)s.getNode()).setFill(Color.RED);
                    ((Ant)s).obtainedFood();

                    return true;
                }
            if(a.hasFood()){
                Circle circle = new Circle();
                circle.setCenterX(a.getStartX() + a.getTranslateX());
                circle.setCenterY(a.getStartY() + a.getTranslateY());
                circle.setRadius(Ant.rad);
                circle.setFill(Color.YELLOW);

                this.root.getChildren().add(circle);
            }
        }
        return false;
    }

    /**
     * Generate the given number of ants at the given location
     * @param amt amount of ants
     * @param x location in x-axis
     * @param y location in y-axis
     */
    public void generateAnts(int amt, int x, int y){
        Ant ant;
        for(int i = 0; i < amt; i++){
            ant = new Ant(x, y,5, 5);
            this.handler.addObject(ant);
            this.root.getChildren().add(0, ant.getNode());
        }
    }

    /**
     * Generates the hive at the selected point
     */
    public void generateHive(){
        Random r = new Random();
        Hive hive = new Hive(this.homeX, this.homeY);
        this.handler.addObject(hive);
        this.root.getChildren().add(0, hive.getNode());
    }

    /**
     * Generate food item at a random location
     */
    public void generateFood(){
        Random r = new Random();
        Food food = new Food(r.nextInt(WIDTH), r.nextInt(HEIGHT));

        this.handler.addObject(food);
        this.root.getChildren().add(0, food.getNode());
    }

    /**
     * Set the number of ants
     * @param ants
     */
    public void setNumAnt(int ants){
        this.numAnt = ants;
    }
}
