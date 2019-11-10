import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

public class AntSimulator extends Simulator{

    private int numAnt; //number of ants to create
    private int homeX, homeY; //location of hive
    private static int velX = 4, velY = 4; //location of hive
    public static LinkedHashMap<Coordinate, PheromoneData> pheromoneLoc;
    public long sec = 0;

    /**
     * Default constructor for Ant simulator
     * @param fps frames per second
     */
    public AntSimulator(int fps){
        super(fps);
        this.numAnt = 500;
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

    public AntSimulator(int numAnt, int homeX, int homeY) {
        this.numAnt = numAnt;
        this.homeX = homeX;
        this.homeY = homeY;
    }

    public AntSimulator(int numAnt, int homeX, int homeY, int velX, int velY) {
        this.numAnt = numAnt;
        this.homeX = homeX;
        this.homeY = homeY;
        AntSimulator.velX = velX;
        AntSimulator.velY = velY;
    }

    /**
     * Initialises the simulator
     * @param primaryStage
     */
    @Override
    public void initialize(final Stage primaryStage){
        primaryStage.setTitle("Ant simulator"); //Set the title of the window
        this.root = new Group(); //create a group to contain sprite objects
        this.scene = new Scene(this.root,WIDTH,HEIGHT,Color.WHITE); //creates a scene to display the objects
        primaryStage.setScene(this.scene); //add the scene to the stage

        Random r = new Random();
        this.homeX = getKeyX(r.nextInt(WIDTH)); //generate random x location for hive
        this.homeY = getKeyY(r.nextInt(HEIGHT)); //generate random y location for the hive
        pheromoneLoc = new LinkedHashMap<>();

        //Generates the item in the simulator
        generateMap(velX, velY);
        generateHive();
        generateAnts(numAnt, this.homeX, this.homeY, velX, velY);
        generateFood();
    }

    @Override
    protected void buildLoop(){

        final Duration oneFrameAmt = Duration.millis(1000/fps);
        final KeyFrame oneFrame = new KeyFrame(oneFrameAmt,
                new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent event) {
                        handler.tick();
                        updatePheromone();
                        checkCollision();
                        handler.cleanUp();
                    }
                });
        Simulator.timeline = new Timeline();
        Simulator.timeline.setCycleCount(Animation.INDEFINITE);
        Simulator.timeline.setAutoReverse(true);
        Simulator.timeline.getKeyFrames().add(oneFrame);

        //http://www.java2s.com/Tutorials/Java/JavaFX/1010__JavaFX_Timeline_Animation.htm
        Simulator.timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                long cur = System.currentTimeMillis()/1000;
                currentTime = cur - startTime;
            }
        };
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
            if(c instanceof Food){
                if(a.collision(c)){
                    ((Circle)s.getNode()).setRadius(3.0);
                    ((Circle)s.getNode()).setFill(Color.RED);
                    a.obtainedFood();
                    PheromoneData p = pheromoneLoc.get(new Coordinate(getKeyX(a.getCurX()), getKeyY(a.getCurY())));
                    p.addFood();
                    return true;
                }
            }
            if(c instanceof Hive){
                if(a.collision(c)){
                    if(a.hasFood()){
                        ((Circle)s.getNode()).setFill(Color.DARKGREEN);
                        ((Circle)s.getNode()).setRadius(1.0);
                        a.droppedFood();
                        PheromoneData p = pheromoneLoc.get(new Coordinate(getKeyX(a.getCurX()), getKeyY(a.getCurY())));
                        p.addFood();
                    }
                    return true;
                }
            }

        }
        return false;
    }

    public void updatePheromone() {
        for (Sprite s : this.handler.getObjects()) {
            if (s instanceof Ant) {
                Ant a = (Ant) s;
                PheromoneData p = pheromoneLoc.get(new Coordinate(getKeyX(a.getCurX()), getKeyY(a.getCurY())));
                if(p != null) {
                    if (a.hasFood()) {
                        p.addFood();
                    } else {
                        p.addHome();
                    }
                }
                else{
                    System.out.println(a.getCurX() + " " + a.getCurY());
                }
            }
        }

        //https://stackoverflow.com/questions/1066589/iterate-through-a-hashmap
        if(sec != currentTime) {
            sec = currentTime;
            Iterator it = pheromoneLoc.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                PheromoneData pd = (PheromoneData) pair.getValue();
                for (Pheromone p : pd.getPheromones()) {
                    p.tick();
                }
            }
        }
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
            this.root.getChildren().add(ant.getNode());
        }
    }

    /**
     * Generates the hive at the selected point
     */
    public void generateHive(){
        Hive hive = new Hive(this.homeX, this.homeY);
        this.handler.addObject(hive);
        this.root.getChildren().add(hive.getNode());
    }

    /**
     * Generate food item at a random location
     */
    public void generateFood(){
        Random r = new Random();
        int x = r.nextInt(Simulator.WIDTH);
        int y = r.nextInt(Simulator.HEIGHT);
        Food food = new Food(x,  y);

        this.handler.addObject(food);
        this.root.getChildren().add(food.getNode());
    }

    /**
     * Generates a HashMap containing pheromone data of the whole window
     * @param velX speed of the ants in x
     * @param velX speed of thr ants in y
     */
    public void  generateMap(int velX, int velY) {
        for (int x = 0; x <= Simulator.WIDTH + velX; x += velX) {
            for (int y = 0; y <= Simulator.HEIGHT + velY; y += velY) {
                PheromoneData p = createPheromones(x, y, velX, velY);
                Coordinate c = new Coordinate(x,y);
                pheromoneLoc.put(c,p);
            }
        }
    }

    public PheromoneData createPheromones(int x, int y, int velX, int velY){
        PheromoneData p = new PheromoneData(x, y, velX, velY);
        Pheromone food = new Pheromone(x, y, velX, velY, "Food",0.5,255,0,0, 10);
        Pheromone home = new Pheromone(x, y, velX, velY, "Home",0.05, 0,0,255, 1);

        p.addPheromone(food);
        p.addPheromone(home);

        this.root.getChildren().add(food.getNode());
        this.root.getChildren().add(home.getNode());
        return p;
    }

    /**
     * Returns the surrounding Pheromone value
     * @param x location in x
     * @param y location in y
     * @return LinkedList of surrounding pheromones
     */
    public static Surrounding getSurrounding(int x, int y){
        Surrounding surrounding = new Surrounding();
        int keyX = (int)(x/velX)*4;
        int keyY = (int)(y/velY)*4;

        if(keyY - velY >= 0){
            if(keyX - velX >= 0){
                surrounding.setNW(pheromoneLoc.get(new Coordinate(keyX-velX, keyY-velY))); //NW
            }

            surrounding.setN(pheromoneLoc.get(new Coordinate(keyX, keyY-velY))); // N

            if(keyX + velX <= Simulator.WIDTH + velX){
                surrounding.setNE(pheromoneLoc.get(new Coordinate(keyX+velX, keyY-velY))); // NE
            }
        }
        if(keyX - velX >= 0){
            surrounding.setW(pheromoneLoc.get(new Coordinate(keyX-velX, keyY))); //W
        }

        surrounding.setC(pheromoneLoc.get(new Coordinate(keyX, keyY))); //Current

        if(keyX + velX >= 0){
            surrounding.setE(pheromoneLoc.get(new Coordinate(keyX+velX, keyY)));
        }

        if(keyY + velY <= HEIGHT + velY){
            if(keyX - velX >= 0){
                surrounding.setSW(pheromoneLoc.get(new Coordinate(keyX-velX, keyY+velY))); //SW
            }

            surrounding.setS(pheromoneLoc.get(new Coordinate(keyX, keyY+velY))); //S

            if(keyX + velX <= Simulator.WIDTH + velX){
                surrounding.setSE(pheromoneLoc.get(new Coordinate(keyX+velX, keyY+velY))); //SE
            }
        }

        return surrounding;
    }

    public static int getKeyX(int x){
        int smaller = (int)(x/velX)*velX;
        int bigger = smaller + velX;
        if(smaller < 0){
            return bigger;
        }
        return smaller;

    }

    public static int getKeyY(int y){
        int smaller = (int)(y/velY)*velY;
        int bigger = smaller + velY;
        if(smaller < 0){
            return bigger;
        }
        return smaller;
    }

    /**
     * Set the number of ants
     * @param ants
     */
    public void setNumAnt(int ants){
        this.numAnt = ants;
    }
}
