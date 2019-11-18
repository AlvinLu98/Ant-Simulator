import javafx.animation.AnimationTimer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.Random;

public class Ant extends Sprite {
    private boolean hasFoodItem;
    private boolean knowFood;
    private boolean foundFood;
    private boolean followFood;
    private boolean dead;
    private long lifespan;
    private Direction prevDirection;
    private AnimationTimer timer;
    private long startTime = System.currentTimeMillis()/1000;
    private long elapsedTime = 0;
    public ArrayList<Integer> blocked;

    public static final double rad = 1.0; //radius of an ant

    /**
     * Constructor for an Ant object
     * @param x location in x-axis
     * @param y location in y-axis
     * @param velX speed in x-axis
     * @param velY speed in y-axis
     */
    public Ant(int x, int y, int velX, int velY) {
        super(x,y,velX, velY);
        this.hasFoodItem = false;
        this.foundFood = false;
        this.prevDirection = Direction.C;
        this.lifespan = 1000000;
        this.dead = false;
        this.blocked = new ArrayList<>();

        Circle circle = drawCircle(x, y, rad);
        circle.setFill(Color.BLACK);
        this.node = circle;

        this.timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                long cur = System.currentTimeMillis()/1000;
                elapsedTime = cur - startTime;
            }
        };
        this.timer.start();
    }

    /**
     * Updates the location of the ant
     */
    @Override
    public void tick() {
        if(hasFoodItem){
            moveWithFood();
        }
        else if(knowFood){
            moveNoFood();
        }
        else{
            moveNoFood();
        }
        if(elapsedTime > lifespan){ //TODO create death probability
            dead = true;
            this.node.setOpacity(0);
        }
    }

    /**
     * Movement when the ant is not holding food
     */
    public void moveNoFood(){
        int direction = findDirectionFood();
        move(direction);
    }

    public void moveWithFood(){
        int direction = findDirectionHome();
        if(direction == 4){
            direction = findDirectionHomeNoPheromone();
        }
        move(direction);
        if((this.c.getX() + node.getTranslateX()) == this.c.getX()
                && (this.c.getY() + node.getTranslateY()) == this.c.getY()){
            ((Circle)this.node).setFill(Color.BLACK);
            ((Circle)this.node).setRadius(rad);
            hasFoodItem = false;
            knowFood = true;
        }
    }

    /**
     *
     * @param direction
     */
    private void move(int direction){
        Random r = new Random();
        int i = 100;
        if(!hasFoodItem || !knowFood){
            i = r.nextInt(100);
        }
        while(blocked.contains(direction)){
            if(direction < 9){
                direction += 1;
            }
            else{
                direction = 0;
            }
        }
        if(i > 0) {
            switch (direction) {
                case 0:
                    moveNW();
                    blocked.clear();
                    break;
                case 1:
                    moveN();
                    blocked.clear();
                    break;
                case 2:
                    moveNE();
                    blocked.clear();
                    break;
                case 3:
                    moveW();
                    blocked.clear();
                    break;
                case 5:
                    moveE();
                    blocked.clear();
                    break;
                case 6:
                    moveSW();
                    blocked.clear();
                    break;
                case 7:
                    moveS();
                    blocked.clear();
                    break;
                case 8:
                    moveSE();
                    blocked.clear();
                    break;
                default:
                    randomPreferredMovement();
                    break;
            }
        }
        else{
            randomPreferredMovement();
        }
    }

    private void printMove(int direction){
        switch (direction) {
            case 0:
                System.out.println(Direction.NW);
                break;
            case 1:
                System.out.println(Direction.N);
                break;
            case 2:
                System.out.println(Direction.NE);
                break;
            case 3:
                System.out.println(Direction.W);
                break;
            case 5:
                System.out.println(Direction.E);
                break;
            case 6:
                System.out.println(Direction.SW);
                break;
            case 7:
                System.out.println(Direction.S);
                break;
            case 8:
                System.out.println(Direction.SE);
                break;
            default:
                System.out.println("Random");
                break;
        }
    }

    public int findDirectionFood(){
        ArrayList<PheromoneData> surrounding = (AntSimulator.getSurrounding(this.getCurX(), this.getCurY()))
                .getInList();
        int moveX = 0;
        int moveY = 0;
        double max = 0;
        int direction = 4;
        ArrayList<Integer> d = prevDirection.getSide(prevDirection);
        for (int i  = 0; i < surrounding.size(); i++) {
            PheromoneData pd = surrounding.get(i);
            if(pd != null){
                for (GroundData p : pd.getPheromones()) {
                    if(p.getName().equals("Obstacle")){
                        blocked.add(i);
                    }
                    else if (p.getName().equals("Food") && i != 4)
                    {
                        Pheromone food = (Pheromone)p;
                        if(food.getValue() > 0) {
                            goingToFood();
                            direction = compareSurroundingFoodForage(pd, food, direction, i, moveX, moveY, max, d);
                            if (direction == i) {
                                moveX = surrounding.get(direction).getStartX();
                                moveY = surrounding.get(direction).getStartY();
                                max = food.getValue();
//                            System.out.println("***Changed***");
                            }
//                        System.out.print(food.getValue() + ": ");
//                        printMove(i);
                        }
                    }
                }
            }
        }

        return direction;
    }

    public int findDirectionHome(){
        ArrayList<PheromoneData> surrounding = (AntSimulator.getSurrounding(this.getCurX(), this.getCurY()))
                .getInList();
        int moveX = 0;
        int moveY = 0;
        double max = 0;
        int direction = 4;
        int defaultDirection = 0;
        double minDist = Integer.MAX_VALUE;
        ArrayList<Integer> d = prevDirection.getSide(prevDirection);
        double current = euclideanDist(this.getCurX(), this.getCurY(), this.getStartX(), this.getStartY());
//        System.out.println("---------------------Potential---------------------");
        for (int i  = 0; i < surrounding.size(); i++) {
            PheromoneData pd = surrounding.get(i);
            if(pd != null){
                double potential = euclideanDist(pd.getStartX(), pd.getStartY(), this.getStartX(), this.getStartY());
                if(potential < current) {
                    if(minDist > potential){
                        defaultDirection = i;
                        minDist = potential;
                    }
                    for (GroundData p : pd.getPheromones()) {
                        if(p.getName().equals("Obstacle")){
                            blocked.add(i);
                        }
                        else if (p.getName().equals("Home")) {
                            Pheromone home = (Pheromone)p;
                            direction = compareSurroundingHome(pd, home, direction, i, moveX, moveY, max, d);
                            moveX = surrounding.get(direction).getStartX();
                            moveY = surrounding.get(direction).getStartY();
                            if(direction == i){
                                max = home.getValue();
                            }
                        }
                    }
                }
            }
        }
        if(max == 0){
            direction = defaultDirection;
//            System.out.println("default");
        }
//        System.out.println("------------------------------------------------");
        return direction;
    }

    //TODO fix with obstacles
    public int findDirectionHomeNoPheromone(){
        ArrayList<PheromoneData> surrounding = (AntSimulator.getSurrounding(this.getCurX(), this.getCurY()))
                .getInList();
        int moveX = 0;
        int moveY = 0;
        double max = 0;
        int direction = 4;
        int defaultDirection = 0;
        double minDist = Integer.MAX_VALUE;
        ArrayList<Integer> d = prevDirection.getSide(prevDirection);
        double current = euclideanDist(this.getCurX(), this.getCurY(), this.getStartX(), this.getStartY());
//        System.out.println("---------------------Potential---------------------");
        for (int i  = 0; i < surrounding.size(); i++) {
            PheromoneData pd = surrounding.get(i);
            if(pd != null){
                double potential = euclideanDist(pd.getStartX(), pd.getStartY(), this.getStartX(), this.getStartY());
                if(potential < current) {
                    if(minDist > potential){
                        direction = i;
                        minDist = potential;
                    }
                }
            }
        }
        return direction;
    }

    private int compareSurroundingFoodForage(PheromoneData pd, Pheromone p, int curD, int i, int moveX, int moveY,
                                             double max, ArrayList<Integer> d)
    {
        int direction = curD;
//        System.out.println("Value: " + p.getValue() + " max: " + max);
        if (max == 0) { //if ant currently has no selected direction
            direction = i;
        }
        else if(p.getValue() >= (max - (p.getValue()*p.getEvaporation()))) {
                //Calculate the euclidean distance of new and old direction to home
                double prev = euclideanDist(moveX, moveY, this.getStartX(), this.getStartY());
                double current = euclideanDist(p.getStartX(), p.getStartY(), this.getStartX(), this.getStartY());
                if (current > prev) { //if new direction is further away from home
                    direction = i;
                }
                else if (current == prev) {
                    if (d.contains(i)) {
                        direction = i;
                    }
                }
        }
        return direction;
    }

    private int compareSurroundingHome(PheromoneData pd, Pheromone p, int curD, int i, int moveX, int moveY, double max,
                                       ArrayList<Integer> d)
    {
        int direction = curD;
//        System.out.println("Value: " + p.getValue() + " max: " + max);
        if(p.getValue() > max) {
            direction = i;
        }
        else if (p.getValue() == max){
            double prev = euclideanDist(pd.getStartX(), pd.getStartY(), moveX, moveY);
            double cur = euclideanDist(pd.getStartX(), pd.getStartY(), this.getCurX(), this.getCurY());
            if(cur > prev){
                direction = i;
            }
        }
        return direction;
    }

    private void randomMovement(){
        Random r = new Random();
        int x = r.nextInt() % 3;
        if (x == 2) {
            boundaryX();
            node.setTranslateX(getTranslateX() + this.velX);
        } else if (x == 1) {
            boundaryNegX();
            node.setTranslateX(getTranslateX() - this.velX);
        }

        int y = r.nextInt() % 3;

        if (y == 2) {
            boundaryY();
            node.setTranslateY(getTranslateY() + this.velY);
        } else if (y == 1) {
            boundaryNegY();
            node.setTranslateY(getTranslateY() - this.velY);
        }
    }

    private void randomPreferredMovement(){
        Random r = new Random();
        int direction = r.nextInt(Integer.MAX_VALUE)%10;
        if(direction < 8){
            int move = r.nextInt(Integer.MAX_VALUE)%3;
            ArrayList<Integer> prev = prevDirection.getSide(prevDirection);
            move(prev.get(move));
        }
        else{
            int move = r.nextInt()%9;
            move(move);
        }
    }

    private void moveNW(){
//        boundaryNegX();
//        node.setTranslateX(getTranslateX() - velX);
//
//        boundaryNegY();
//        node.setTranslateY(getTranslateY() - velY);

        if(boundaryNegX() && boundaryNegY()){
            moveSE();
        }
        else if(boundaryNegX()){
            moveNE();
        }
        else if(boundaryNegY()){
            moveSW();
        }
        else {
            node.setTranslateX(getTranslateX() - velX);
            node.setTranslateY(getTranslateY() - velY);
            prevDirection = Direction.NW;
        }
    }

    private void moveN(){
//        boundaryNegY();
//        node.setTranslateY(getTranslateY() - velY);

        if(boundaryNegY()){
            moveS();
        }
        else {
            node.setTranslateY(getTranslateY() - velY);
            prevDirection = Direction.N;
        }
    }

    private void moveNE(){
//        boundaryX();
//        node.setTranslateX(getTranslateX() + velX);
//        boundaryNegY();
//        node.setTranslateY(getTranslateY() - velY);

        if(boundaryX() && boundaryNegY()){
            moveSW();
        }
        else if(boundaryX()){
            moveNW();
        }
        else if(boundaryNegY()){
            moveSE();
        }
        else {
            node.setTranslateX(getTranslateX() + velX);
            node.setTranslateY(getTranslateY() - velY);
            prevDirection = Direction.NE;
        }
    }

    private void moveW(){
//        boundaryNegX();
//        node.setTranslateX(getTranslateX() - velX);

        if(boundaryNegX()){
            moveE();
        }
        else {
            node.setTranslateX(getTranslateX() - velX);
            prevDirection = Direction.W;
        }
    }

    private void moveE(){
//        boundaryX();
//        node.setTranslateX(getTranslateX() + velX);

        if(boundaryX()){
            moveW();
        }
        else {
            node.setTranslateX(getTranslateX() + velX);
            prevDirection = Direction.E;
        }
    }

    private void moveSW(){
//        boundaryNegX();
//        node.setTranslateX(getTranslateX() - velX);
//        boundaryY();
//        node.setTranslateY(getTranslateY() + velY);

        if(boundaryNegX() && boundaryY()){
            moveNE();
        }
        else if(boundaryNegX()){
            moveSE();
        }
        else if(boundaryY()){
            moveNW();
        }
        else {
            node.setTranslateX(getTranslateX() - velX);
            node.setTranslateY(getTranslateY() + velY);
            prevDirection = Direction.SW;
        }
    }

    private void  moveS(){
//        boundaryY();
//        node.setTranslateY(getTranslateY() + velY);

        if(boundaryY()){
            moveN();
        }
        else {
            node.setTranslateY(getTranslateY() + velY);
            prevDirection = Direction.S;
        }
    }

    private void  moveSE(){
//        boundaryX();
//        node.setTranslateX(getTranslateX() + velX);
//        boundaryY();
//        node.setTranslateY(getTranslateY() + velY);

        if(boundaryX() && boundaryY()){
            moveNW();
        }
        else if(boundaryX()){
            moveSW();
        }
        else if(boundaryY()){
            moveNE();
        }
        else {
            node.setTranslateX(getTranslateX() + velX);
            node.setTranslateY(getTranslateY() + velY);
            prevDirection = Direction.SE;
        }
    }

    /**
     * https://stackoverflow.com/questions/15013913/checking-collision-of-shapes-with-javafx
     * @param other
     * @return
     */
    @Override
    public boolean collision(Sprite other) {
        if(other instanceof Food){
            return foodCollision((Food)other);
        }
        if(other instanceof Hive){
            return hiveCollision((Hive)other);
        }
        return false;
    }

    /**
     * check if ant is colliding with another ant
     * @param other another ant
     * @return true if ants are colliding
     */
    public boolean antCollision(Ant other){
        Circle thisAnt = this.getBounds(rad);
        Circle otherAnt = other.getBounds(rad);
        return thisAnt.getBoundsInParent().intersects(otherAnt.getBoundsInParent());
    }

    /**
     * Check if ant is colliding with food
     * @param other food item
     * @return true if ant collided with the food
     */
    public boolean foodCollision(Food other){
        Circle thisAnt = this.getBounds(rad);
        Circle food = other.getBounds(Food.rad);
        return thisAnt.getBoundsInParent().intersects(food.getBoundsInParent());
    }

    public boolean hiveCollision(Hive other){
        Circle thisAnt = this.getBounds(rad);
        Circle hive = other.getBounds(Food.rad);
        return thisAnt.getBoundsInParent().intersects(hive.getBoundsInParent());
    }

    /**
     * Set hasFoodItem to true
     */
    public void obtainedFood(){
        this.hasFoodItem = true;
        this.followFood = false;
    }

    public void droppedFood(){
        this.hasFoodItem = false;
    }

    /**
     * Check if the ant has food
     * @return true if ant hasFood
     */
    public boolean hasFood(){
        return this.hasFoodItem;
    }

    public boolean isDead(){
        return this.dead;
    }

    public boolean isFollowingFood(){
        return this.followFood;
    }

    public void goingToFood(){
        this.followFood = true;
    }
}
