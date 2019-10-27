import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

public class AntSimulator extends Simulator{

    private int numAnt; //number of ants to create
    private int homeX, homeY; //location of hive
    public static HashMap<Coordinate, PheromoneData> pheromoneLoc;

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
        pheromoneLoc = new HashMap<>();

        //Generates the item in the simulator
        generateHive();
        generateAnts(numAnt, this.homeX, this.homeY, 4, 4);
        generateFood();
        generateMap(4,4);
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
    public void generateAnts(int amt, int x, int y, int velX, int velY){
        Ant ant;
        for(int i = 0; i < amt; i++){
            ant = new Ant(x, y, velX, velY);
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
     * Generates a HashMap containing pheromone data of the whole window
     * @param velX speed of the ants in x
     * @param velX speed of thr ants in y
     */
    public void  generateMap(int velX, int velY) {
        for (int x = velX / 2; x <= Simulator.WIDTH; x += velX) {
            for (int y = velY / 2; y <= Simulator.HEIGHT; y += velY) {
                PheromoneData p = createPheromones(x, y, velX, velY);
                Coordinate c = new Coordinate(x,y);
                pheromoneLoc.put(c,p);
            }
        }
    }

    public PheromoneData createPheromones(int x, int y, int velX, int velY){
        PheromoneData p = new PheromoneData(x, y);
        p.addPheromone(new Pheromone(x,y,velX, velY,"Food",255,0,0));
        p.addPheromone(new Pheromone(x,y,velX, velY,"Home",0,0,255));
        //TODO add the shapes into the root
        return p;
    }

    /**
     * Returns the surrounding Pheromone value
     * @param x location in x
     * @param y location in y
     * @return LinkedList of surrounding pheromones
     */
    public static LinkedList<PheromoneData> getSurrounding(int x, int y){
        //TODO get the area around the given point;
        //use divide to get the point?
        return null;
    }

    /**
     * Set the number of ants
     * @param ants
     */
    public void setNumAnt(int ants){
        this.numAnt = ants;
    }
}
