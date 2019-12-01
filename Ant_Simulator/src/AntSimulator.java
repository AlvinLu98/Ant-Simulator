import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

public class AntSimulator extends Simulator{

    /* ------------------------------ Simulator data ------------------------------*/

    private int numAnt, numFood = 1; //number of ants to create
    private int homeX, homeY; //location of hive
    private double evaporationRate = 0.9;
    private static int velX = 4, velY = 4; //location of hive
    private static LinkedHashMap<Coordinate, PheromoneData> pheromoneLoc;
    private ObstacleData od;
    private String obstacleType;

    private boolean setUpHive = true, setUpFood = false;

    /* ---------------------------------------------------------------------------*/

    /* ------------------------------ Constructors ------------------------------*/

    /**
     * Default constructor for Ant simulator
     * @param fps frames per second
     */
    AntSimulator(int fps){
        super(fps);
        this.numAnt = 500;
        this.od = new ObstacleData((Simulator.WIDTH/velX)+velX, (Simulator.HEIGHT/velY)+velY);
    }

    /**
     * Creates an ant simulator specified number of ants and fps
     * @param fps frames per second
     * @param ants number of ants
     */
    public AntSimulator(int fps, int ants){
        super(fps);
        this.numAnt = ants;
        this.od = new ObstacleData((Simulator.WIDTH/velX)+velX, (Simulator.HEIGHT/velY)+velY);
    }

    /**
     * Creates an ant simulator with a given home location and number of ants;
     * @param numAnt number of ants for the simulator
     * @param homeX x location of the hive
     * @param homeY y location of the hive
     */
    public AntSimulator(int numAnt, int homeX, int homeY) {
        this.numAnt = numAnt;
        this.homeX = homeX;
        this.homeY = homeY;
        od = new ObstacleData((Simulator.WIDTH/velX)+velX, (Simulator.HEIGHT/velY)+velY);
    }

    /**
     * Creates an ant simulator with specified home location, number of ants and their velocity
     * @param numAnt number of ants
     * @param homeX x location of home
     * @param homeY y location of home
     * @param velX x velocity
     * @param velY y velocity
     */
    public AntSimulator(int numAnt, int homeX, int homeY, int velX, int velY) {
        this.numAnt = numAnt;
        this.homeX = homeX;
        this.homeY = homeY;
        AntSimulator.velX = velX;
        AntSimulator.velY = velY;
        od = new ObstacleData((Simulator.WIDTH/velX)+velX, (Simulator.HEIGHT/velY)+velY);
    }

    /* ---------------------------------------------------------------------------*/

    /* ------------------------------ Overridden methods ------------------------------*/

    /**
     * Initialises the scene
     * @param primaryStage stage where the scene will be initialised
     */
    @Override
    public void initialize(final Stage primaryStage){
        primaryStage.setTitle("Ant simulator"); //Set the title of the window

        Rectangle2D screenSize = Screen.getPrimary().getBounds();
        primaryStage.setX(screenSize.getMinX() + 20);
        primaryStage.setY(screenSize.getMinY() + 20);

        this.root = new Group(); //create a group to contain sprite objects
        this.scene = new Scene(this.root,WIDTH,HEIGHT,Color.WHITE); //creates a scene to display the objects
        primaryStage.setScene(this.scene); //add the scene to the stage

        createEvents();

        Random r = new Random();
        do {
            this.homeX = getKeyX(r.nextInt(WIDTH)); //generate random x location for hive
            this.homeY = getKeyY(r.nextInt(HEIGHT)); //generate random y location for the hive
        }while(isWithinObstacle(this.homeX, this.homeY));
        pheromoneLoc = new LinkedHashMap<>();
        //Generates the item in the simulator
//        generateMenuBar();
        generateMap(velX, velY);
        generateHive();
        generateAnts(numAnt, this.homeX, this.homeY, velX, velY);
        generateRandomFood(numFood);
    }

    /**
     * Builds the frame loop
     */
    @Override
    protected void buildLoop(){

        final Duration oneFrameAmt = Duration.millis(1000/fps);
        final KeyFrame oneFrame = new KeyFrame(oneFrameAmt,
                event -> {
                    handler.tick();
                    updatePheromone();
                    checkCollision();
                    handler.cleanUp();
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
     * Handles collision detection and collision pre-processing
     * @param s current sprite
     * @param c sprite to check collision against
     * @return true if there is collision between the sprites
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

    /* --------------------------------------------------------------------------------*/

    private void createEvents(){
        root.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println(event.getSceneX());
                System.out.println(event.getSceneY());

                int x = getKeyX((int)event.getSceneX());
                int y = getKeyY((int)event.getSceneY());
                Coordinate c = new Coordinate(x, y);

                if(setUpHive){
                    homeX = x;
                    homeY = y;
                }
                else if(setUpFood){
                    if(!generateFood(x, y)){
                        System.out.println("Invalid location!");
                    }
                }
            }
        });
    }

    private void updatePheromone() {
        for (Sprite s : this.handler.getObjects()) {
            if (s instanceof Ant) {
                Ant a = (Ant) s;
                PheromoneData p = pheromoneLoc.get(new Coordinate(getKeyX(a.getCurX()), getKeyY(a.getCurY())));
                if(p != null) {
                    if (a.hasFood()) {
                        p.addFood();
                    }
                    else if(!a.isFollowingFood()) {
                        p.addHome();
                    }
                }
                else{
                    System.out.println(a.getCurX() + " " + a.getCurY());
                }
            }
        }

        //https://stackoverflow.com/questions/1066589/iterate-through-a-hashmap
//        if(sec != currentTime) {
//            sec = currentTime;
        for (Map.Entry<Coordinate, PheromoneData> coordinatePheromoneDataEntry : pheromoneLoc.entrySet()) {
            PheromoneData pd = (PheromoneData) ((Map.Entry) coordinatePheromoneDataEntry).getValue();
            for (GroundData p : pd.getPheromones()) {
                p.tick();
            }
        }
//        }
    }

    private void generateMenuBar(){
        MenuBar menu = new MenuBar();

        Menu file = new Menu("File");
        Menu edit = new Menu("Edit");
        Menu view = new Menu("View");

        menu.getMenus().addAll(file, edit, view);
        VBox box = new VBox(menu);
        this.root.getChildren().add(box);
    }

    /**
     * Generate the given number of ants at the given location
     * @param amt amount of ants
     * @param x location in x-axis
     * @param y location in y-axis
     */
    private void generateAnts(int amt, int x, int y, int velX, int velY){
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
    private void generateHive(){
        Hive hive = new Hive(this.homeX, this.homeY);
        this.handler.addObject(hive);
        this.root.getChildren().add(hive.getNode());
    }

    /**
     * Generate food item at a random location
     */
    private void generateRandomFood(int amt){
        for(int i = 0; i < amt; i++) {
            Random r = new Random();
            int x, y;
            do{
                x = r.nextInt(Simulator.WIDTH);
                y = r.nextInt(Simulator.HEIGHT);
            }while(isWithinObstacle(x,y));
            Food food = new Food(x, y);

            this.handler.addObject(food);
            this.root.getChildren().add(food.getNode());
        }
    }

    public boolean generateFood(int x, int y){
        if(!isWithinObstacle(x, y)){
            Food food = new Food(x, y);

            this.handler.addObject(food);
            this.root.getChildren().add(food.getNode());
            return true;
        }
        return false;
    }

    private boolean isWithinObstacle(int x, int y){
        boolean[][] points = od.getPoints();
        return points[getKeyX(x)/velX][getKeyY(y)/velY];
    }

    /**
     * Generates a HashMap containing pheromone data of the whole window
     * @param velX speed of the ants in x
     * @param velY speed of thr ants in y
     */
    private void  generateMap(int velX, int velY) {
        if(obstacleType.equals("One Obstacle")){
            od.oneObstacle();
        }
        else if(obstacleType.equals("Two Obstacle")){
            od.twoObstacle();
        }
        for (int x = 0; x <= Simulator.WIDTH + velX; x += velX) {
            for (int y = 0; y <= Simulator.HEIGHT + velY; y += velY) {
                PheromoneData p = new PheromoneData(x,y,velX,velY);
                if(isWithinObstacle(x,y)){
                    Obstacle o = new Obstacle(x, y, velX, velY, 205, 133, 63);
                    p.addData(o);
                    this.root.getChildren().add(o.getNode());
                }
                else{
                    p = createPheromones(x, y, velX, velY);
                }
                Coordinate c = new Coordinate(x,y);
                pheromoneLoc.put(c,p);
            }
        }
    }

    private PheromoneData createPheromones(int x, int y, int velX, int velY){
        PheromoneData p = new PheromoneData(x, y, velX, velY);
        Pheromone food = new Pheromone(x, y, velX, velY, "Food",0.3,255,0,0, 10,
                evaporationRate);
        Pheromone home = new Pheromone(x, y, velX, velY, "Home",0.05, 0,0,255, 1,
                evaporationRate);

        p.addData(food);
        p.addData(home);

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
    static Surrounding getSurrounding(int x, int y){
        Surrounding surrounding = new Surrounding();
        int keyX = (x/velX) *4;
        int keyY = (y/velY) *4;

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

    private static int getKeyX(int x){
        int smaller = (x/velX) *velX;
        int bigger = smaller + velX;
        if(smaller < 0){
            return bigger;
        }
        return smaller;

    }

    private static int getKeyY(int y){
        int smaller = (y/velY) *velY;
        int bigger = smaller + velY;
        if(smaller < 0){
            return bigger;
        }
        return smaller;
    }

    /**
     * Set the number of ants
     * @param ants Number of ants
     */
    public void setNumAnt(int ants){
        this.numAnt = ants;
    }

    public double getEvaporationRate() {
        return evaporationRate;
    }

    public void setEvaporationRate(double evaporationRate) {
        this.evaporationRate = evaporationRate;
    }

    public int getNumFood() {
        return numFood;
    }

    public void setNumFood(int numFood) {
        this.numFood = numFood;
    }

    public String getObstacleType() {
        return obstacleType;
    }

    public void setObstacleType(String obstacleType) {
        this.obstacleType = obstacleType;
    }
}
