import javafx.animation.AnimationTimer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.Random;

public class Ant extends Sprite {
    private boolean hasFoodItem;
    private boolean knowFood;
    private boolean foundFood;
    private boolean dead;
    private long lifespan;
    private Direction prevDirection;
    private AnimationTimer timer;
    private long startTime = System.currentTimeMillis()/1000;
    private long elapsedTime = 0;

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

        Circle circle = drawCircle(x, y, this.rad);
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
        //TODO remember previous location
        if(hasFoodItem){
            moveWithFood();
        }
        else if(knowFood){
            moveNoFood();
        }
        else{
            moveNoFood();
        }
        if(elapsedTime > lifespan){
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

    /**
     * Movement pattern of ants with food to go back home
     * Assumes ants know exactly the way back home
     */
    public void moveWithFoodHome(){
        if(velX < 0){
            velX *= -1;
        }
        if(velY < 0){
            velY *= -1;
        }

        ((Circle)this.node).setFill(Color.RED);

        if((this.c.getX() + this.node.getTranslateX()) < this.c.getX()){
            node.setTranslateX(getTranslateX() + velX);
        }
        else if ((this.c.getX() + this.node.getTranslateX()) > this.c.getX()){
            node.setTranslateX(getTranslateX() - velX);
        }

        if((this.c.getY() + this.node.getTranslateY()) < this.c.getY()){
            node.setTranslateY(getTranslateY() + velY);
        }
        else if((this.c.getY() + this.node.getTranslateY()) > this.c.getY()){
            node.setTranslateY(getTranslateY() - velY);
        }

        if((this.c.getX() + node.getTranslateX()) == this.c.getX()
                && (this.c.getY() + node.getTranslateY()) == this.c.getY()){
            ((Circle)this.node).setFill(Color.DARKGREEN);
            hasFoodItem = false;
            knowFood = true;
        }
    }


    public void moveWithFood(){
        int direction = findDirectionHome();
        move(direction);
        if((this.c.getX() + node.getTranslateX()) == this.c.getX()
                && (this.c.getY() + node.getTranslateY()) == this.c.getY()){
            ((Circle)this.node).setFill(Color.DARKGREEN);
            hasFoodItem = false;
            knowFood = true;
        }
    }

    private void move(int direction){
        switch(direction){
            case 0:
                moveNW();
                prevDirection = Direction.NW;
                break;
            case 1:
                moveN();
                prevDirection = Direction.N;
                break;
            case 2:
                moveNE();
                prevDirection = Direction.NE;
                break;
            case 3:
                moveW();
                prevDirection = Direction.W;
                break;
            case 5:
                moveE();
                prevDirection = Direction.E;
                break;
            case 6:
                moveSW();
                prevDirection = Direction.SW;
                break;
            case 7:
                moveS();
                prevDirection = Direction.S;
                break;
            case 8:
                moveSE();
                prevDirection = Direction.SE;
                break;
            default:
                randomMovement();
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
                for (Pheromone p : pd.getPheromones()) {
                    if (p.getName().equals("Food") && p.getValue() > 0) {
                        direction = compareSurroundingFoodForage(pd, p, direction, i, moveX, moveY, max, d);
                        if(direction == i){
                            moveX = surrounding.get(direction).getStartX();
                            moveY = surrounding.get(direction).getStartY();
                            max = p.getValue();
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
        int direction = prevDirection.getNum();
        ArrayList<Integer> d = prevDirection.getSide(prevDirection);
        for (int i  = 0; i < surrounding.size(); i++) {
            PheromoneData pd = surrounding.get(i);
            if(pd != null){
                for (Pheromone p : pd.getPheromones()) {
                    if (p.getName().equals("Home") && p.getValue() > 0) {
                        direction = compareSurroundingHome(pd, p, direction, i, moveX, moveY, max, d);
                        moveX = surrounding.get(direction).getStartX();
                        moveY =surrounding.get(direction).getStartY();
                        max = p.getValue();
                    }
                }
            }
        }
        return direction;
    }

    public int findDirectionFoodHome(){
        ArrayList<PheromoneData> surrounding = (AntSimulator.getSurrounding(this.getCurX(), this.getCurY()))
                .getInList();
        int moveX = 0;
        int moveY = 0;
        double max = 0;
        int direction = prevDirection.getNum();
        ArrayList<Integer> d = prevDirection.getSide(prevDirection);
        for (int i  = 0; i < surrounding.size(); i++) {
            PheromoneData pd = surrounding.get(i);
            if(pd != null){
                for (Pheromone p : pd.getPheromones()) {
                    if (p.getName().equals("Food") && p.getValue() > 0) {
                        direction = compareSurroundingFoodHome(pd, p, direction, i, moveX, moveY, max, d);
                        moveX = surrounding.get(direction).getStartX();
                        moveY =surrounding.get(direction).getStartY();
                        max = p.getValue() * 10;
                    }
                    else if (p.getName().equals("Home") && p.getValue() > 0) {
                        direction = compareSurroundingHome(pd, p, direction, i, moveX, moveY, max, d);
                        moveX = surrounding.get(direction).getStartX();
                        moveY =surrounding.get(direction).getStartY();
                        max = p.getValue();
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
        if (max == 0) { //if ant currently has no selected direction
            direction = i;
        }
        //if new direction has equal or greater pheromone value
        else if(p.getValue() >= (max - (p.getValue()*p.getEvaporation()))) {
            //if new direction has equal pheromone value
            if(p.getValue() == (max - (p.getValue()*p.getEvaporation()))){
                //Calculate the euclidean distance of new and old direction to home
                double prev = euclideanDist(this.getStartX(), this.getStartY(), moveX, moveY);
                double current = euclideanDist(this.getStartX(), this.getStartY(),
                        p.getStartX(), p.getStartY());
                if(current >= prev){ //if new direction is further away from home
                    direction = i;
                }
                else if(current == prev){
                    if(d.contains(i)){
                        direction = i;
                    }
                }
            }
            else{ //if new direction has greater pheromone value
                direction = i;
            }
        }
        return direction;
    }

    private int compareSurroundingFoodHome(PheromoneData pd, Pheromone p, int curD, int i, int moveX, int moveY,
                                           double max, ArrayList<Integer> d)
    {
        int direction = curD;
        if (max == 0) {
            direction = i;
        }
        else if(p.getValue() >= max) {
            if(p.getValue() == max){
                double prev = euclideanDist(this.getStartX(), this.getStartY(), moveX, moveY);
                double current = euclideanDist(this.getStartX(), this.getStartY(),
                        pd.getStartX(), pd.getStartY());
                if(current < prev){
                    direction = i;
                }
                else if(current == prev){
                    if(d.contains(i)){
                        direction = i;
                    }
                }
            }
            else{
                direction = i;
            }
        }
        return direction;
    }

    private int compareSurroundingHome(PheromoneData pd, Pheromone p, int curD, int i, int moveX, int moveY, double max,
                                       ArrayList<Integer> d)
    {
        int direction = curD;
        if (max == 0) {
            direction = i;
        }
//        else if(p.getValue() >= max) {
//            if(p.getValue() == max){
                double prev = euclideanDist(this.getStartX(), this.getStartY(), moveX, moveY);
                double current = euclideanDist(this.getStartX(), this.getStartY(),
                        pd.getStartX(), pd.getStartY());
                if(current < prev){
                    direction = i;
                }
                else if(current == prev){
                    if(d.contains(i)){
                        direction = i;
                    }
                }
//            }
//            else{
//                direction = i;
//            }
//        }
        return direction;
    }

    public void randomMovement(){
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

    public void moveNW(){
        boundaryNegX();
        node.setTranslateX(getTranslateX() - velX);
        boundaryNegY();
        node.setTranslateY(getTranslateY() - velY);
    }

    public void moveN(){
        boundaryNegY();
        node.setTranslateY(getTranslateY() - velY);
    }

    public void moveNE(){
        boundaryX();
        node.setTranslateX(getTranslateX() + velX);
        boundaryNegY();
        node.setTranslateY(getTranslateY() - velY);
    }

    public void moveW(){
        boundaryNegX();
        node.setTranslateX(getTranslateX() - velX);
    }

    public void moveE(){
        boundaryX();
        node.setTranslateX(getTranslateX() + velX);
    }

    public void moveSW(){
        boundaryNegX();
        node.setTranslateX(getTranslateX() - velX);
        boundaryY();
        node.setTranslateY(getTranslateY() + velY);
    }

    public void  moveS(){
        boundaryY();
        node.setTranslateY(getTranslateY() + velY);
    }

    public void  moveSE(){
        boundaryX();
        node.setTranslateX(getTranslateX() + velX);
        boundaryY();
        node.setTranslateY(getTranslateY() + velY);
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
        if(thisAnt.getBoundsInParent().intersects(otherAnt.getBoundsInParent())){
            return true;
        }
        return false;
    }

    /**
     * Check if ant is colliding with food
     * @param other food item
     * @return true if ant collided with the food
     */
    public boolean foodCollision(Food other){
        Circle thisAnt = this.getBounds(rad);
        Circle food = other.getBounds(Food.rad);
        if(thisAnt.getBoundsInParent().intersects(food.getBoundsInParent())){
            return true;
        }
        return false;
    }

    public boolean hiveCollision(Hive other){
        Circle thisAnt = this.getBounds(rad);
        Circle hive = other.getBounds(Food.rad);
        if(thisAnt.getBoundsInParent().intersects(hive.getBoundsInParent())){
            return true;
        }
        return false;
    }

    /**
     * Set hasFoodItem to true
     */
    public void obtainedFood(){
        this.hasFoodItem = true;
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
}
