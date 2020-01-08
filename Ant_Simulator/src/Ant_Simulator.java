import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

public class Ant_Simulator extends Simulator{

    /* ---------------------------------- Simulator data ----------------------------------*/

    private int numAnt, numFood = 1; //number of ants to create
    private int homeX, homeY; //location of hive
    private double evaporationRate = 0.9;
    private static int velX = 4, velY = 4; //location of hive
    private static LinkedHashMap<Coordinate, Pheromone_Data> pheromoneLoc;
    private ArrayList<Coordinate> foodCoordinate = new ArrayList<>();
    private Obstacle_Data od;
    private String obstacleType;

    private boolean setUpHive = false, setUpFood = false;
    private int foodCount = 0;
    private Parent message;

    /* ------------------------------------------------------------------------------------*/

    /* ----------------------------------- Constructors -----------------------------------*/

    /**
     * Default constructor for Ant simulator
     * @param fps frames per second
     */
    Ant_Simulator(int fps){
        super(fps);
        this.numAnt = 500;
        this.od = new Obstacle_Data((Simulator.WIDTH/velX)+velX, (Simulator.HEIGHT/velY)+velY);
    }

    /**
     * Creates an ant simulator specified number of ants and fps
     * @param fps frames per second
     * @param ants number of ants
     */
    public Ant_Simulator(int fps, int ants){
        super(fps);
        this.numAnt = ants;
        this.od = new Obstacle_Data((Simulator.WIDTH/velX)+velX, (Simulator.HEIGHT/velY)+velY);
    }

    /**
     * Creates an ant simulator with a given home location and number of ants;
     * @param numAnt number of ants for the simulator
     * @param homeX x location of the hive
     * @param homeY y location of the hive
     */
    public Ant_Simulator(int numAnt, int homeX, int homeY) {
        this.numAnt = numAnt;
        this.homeX = homeX;
        this.homeY = homeY;
        od = new Obstacle_Data((Simulator.WIDTH/velX)+velX, (Simulator.HEIGHT/velY)+velY);
    }

    /**
     * Creates an ant simulator with specified home location, number of ants and their velocity
     * @param numAnt number of ants
     * @param homeX x location of home
     * @param homeY y location of home
     * @param velX x velocity
     * @param velY y velocity
     */
    public Ant_Simulator(int numAnt, int homeX, int homeY, int velX, int velY) {
        this.numAnt = numAnt;
        this.homeX = homeX;
        this.homeY = homeY;
        Ant_Simulator.velX = velX;
        Ant_Simulator.velY = velY;
        od = new Obstacle_Data((Simulator.WIDTH/velX)+velX, (Simulator.HEIGHT/velY)+velY);
    }

    /* -------------------------------------------------------------------------------------*/

    /* -------------------------------- Overridden methods ---------------------------------*/

    /**
     * Initialises the scene
     * @param primaryStage stage where the scene will be initialised
     */
    @Override
    public void initialize(final Stage primaryStage){
        /* ------------------------------ Load from XML ------------------------------ */
        try{
            message = FXMLLoader.load(getClass().getResource("Map_Creation.fxml"));
        }catch (IOException e){
            System.out.println("Failed to load Set up fxml file");
        }
        /* --------------------------------------------------------------------------- */

        this.rootNode = new Group();
        this.scene = new Scene(this.rootNode,WIDTH,HEIGHT,Color.WHITE); //creates a scene to display the objects
        primaryStage.setScene(this.scene); //add the scene to the stage
        primaryStage.setTitle("Ant simulator"); //Set the title of the window

        Rectangle2D screenSize = Screen.getPrimary().getBounds();
        primaryStage.setX(screenSize.getMinX() + 20);
        primaryStage.setY(screenSize.getMinY() + 20);

        pheromoneLoc = new LinkedHashMap<>();
        generateMap(velX, velY);
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
                    Pheromone_Data p = pheromoneLoc.get(new Coordinate(getKeyX(a.getCurX()), getKeyY(a.getCurY())));
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
                        Pheromone_Data p = pheromoneLoc.get(new Coordinate(getKeyX(a.getCurX()), getKeyY(a.getCurY())));
                        p.addFood();
                    }
                    return true;
                }
            }

        }
        return false;
    }

    /* -------------------------------------------------------------------------------------*/

    /* ------------------------------------ Setting up -------------------------------------*/
    public void setUp(){
        selectLocation();

        final Duration oneFrameAmt = Duration.millis(1000/fps);
        final KeyFrame oneFrame = new KeyFrame(oneFrameAmt,
                event -> {

                });
        Simulator.timeline = new Timeline();
        Simulator.timeline.setCycleCount(Animation.INDEFINITE);
        Simulator.timeline.setAutoReverse(true);
        Simulator.timeline.getKeyFrames().add(oneFrame);
        Simulator.timeline.play();
    }

    public void selectLocation(){
        rootNode.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                int x = getKeyX((int)event.getSceneX());
                int y = getKeyY((int)event.getSceneY());

                if(!isWithinObstacle(x,y)){
                    if(!setUpHive) {
                        homeX = x;
                        homeY = y;
                        generateHive();
                        setUpHive = true;
                        ((Text)message.lookup("#instruction")).setText("Please select Food location(s)");
                    }
                    else if(!setUpFood){
                        Coordinate foodLoc = new Coordinate(x,y);
                        foodCoordinate.add(foodLoc);
                        generateFood(x,y);
                        foodCount++;
                        if(foodCount == numFood){
                            setUpFood = true;
                            begin();
                        }
                    }
                }
                else{
                    Alert a = new Alert(Alert.AlertType.WARNING);
                    a.setContentText("Invalid location!");
                }
            }
        });
    }

    public void begin(){
        timeline.stop();
        generateAnts(numAnt, homeX, homeY, velX, velY);
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Menu.fxml"));

            Rectangle2D screenSize = Screen.getPrimary().getBounds();
            Scene scene = new Scene(root, 500, 500);
            Display.menuStage.setTitle("Settings");
            Display.menuStage.setX(screenSize.getMinX() + screenSize.getMaxX() - 700);
            Display.menuStage.setY(screenSize.getMinY() + 20);
            Display.menuStage.setScene(scene);
            Display.menuStage.show();

            Menu_Controller.start();

        }catch (IOException e){
            System.out.println("Failed to load Menu fxml file");
        }
        buildLoop();
        beginSimulation();
    }

    public void reset(Stage primaryStage){
        this.handler.emptySprites();
        this.rootNode = new Group();
        this.scene = new Scene(this.rootNode,WIDTH,HEIGHT,Color.WHITE); //creates a scene to display the objects
        primaryStage.setScene(this.scene); //add the scene to the stage
        primaryStage.setTitle("Ant simulator"); //Set the title of the window

        Rectangle2D screenSize = Screen.getPrimary().getBounds();
        primaryStage.setX(screenSize.getMinX() + 20);
        primaryStage.setY(screenSize.getMinY() + 20);

        pheromoneLoc = new LinkedHashMap<>();
        generateMap(velX, velY);
        generateHive();
        for(Coordinate c: foodCoordinate){
            generateFood(c.getX(), c.getY());
        }
        generateAnts(numAnt, homeX, homeY, velX, velY);
    }

    /* --------------------------------------------------------------------------------*/

    private void updatePheromone() {
        for (Sprite s : this.handler.getObjects()) {
            if (s instanceof Ant) {
                Ant a = (Ant) s;
                Pheromone_Data p = pheromoneLoc.get(new Coordinate(getKeyX(a.getCurX()), getKeyY(a.getCurY())));
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

        for (Map.Entry<Coordinate, Pheromone_Data> coordinatePheromoneDataEntry : pheromoneLoc.entrySet()) {
            Pheromone_Data pd = (Pheromone_Data) ((Map.Entry) coordinatePheromoneDataEntry).getValue();
            for (Ground_Data p : pd.getPheromones()) {
                p.tick();
            }
        }
    }

    private void generateMenuBar(){
        MenuBar menu = new MenuBar();

        Menu file = new Menu("File");
        Menu edit = new Menu("Edit");
        Menu view = new Menu("View");

        menu.getMenus().addAll(file, edit, view);
        VBox box = new VBox(menu);
        this.rootNode.getChildren().add(box);
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
            this.rootNode.getChildren().add(ant.getNode());
        }
    }

    /**
     * Generates the hive at the selected point
     */
    private void generateHive(){
        Hive hive = new Hive(this.homeX, this.homeY);
        this.handler.addObject(hive);
        this.rootNode.getChildren().add(hive.getNode());
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
            this.rootNode.getChildren().add(food.getNode());
        }
    }

    public boolean generateFood(int x, int y){
        if(!isWithinObstacle(x, y)){
            Food food = new Food(x, y);

            this.handler.addObject(food);
            this.rootNode.getChildren().add(food.getNode());
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
                Pheromone_Data p = new Pheromone_Data(x,y,velX,velY);
                if(isWithinObstacle(x,y)){
                    Obstacle o = new Obstacle(x, y, velX, velY, 205, 133, 63);
                    p.addData(o);
                    this.rootNode.getChildren().add(o.getNode());
                }
                else{
                    p = createPheromones(x, y, velX, velY);
                }
                Coordinate c = new Coordinate(x,y);
                pheromoneLoc.put(c,p);
            }
        }
    }

    private Pheromone_Data createPheromones(int x, int y, int velX, int velY){
        Pheromone_Data p = new Pheromone_Data(x, y, velX, velY);
        Pheromone food = new Pheromone(x, y, velX, velY, "Food",0.3,255,0,0, 10,
                evaporationRate);
        Pheromone home = new Pheromone(x, y, velX, velY, "Home",0.05, 0,0,255, 1,
                evaporationRate);

        p.addData(food);
        p.addData(home);

        this.rootNode.getChildren().add(food.getNode());
        this.rootNode.getChildren().add(home.getNode());
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