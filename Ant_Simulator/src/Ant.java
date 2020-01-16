import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class Ant extends Creature{
    private boolean hasFoodItem;
    private boolean knowFood;
    private boolean followFood;
    private Direction prevDirection;
    private int behind;
    public ArrayList<Integer> blocked;
    private LinkedList<Coordinate> visited = new LinkedList<>();
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
        this.prevDirection = Direction.C;
        this.dead = false;
        this.blocked = new ArrayList<>();

        Circle circle = drawCircle(x, y, rad);
        circle.setFill(Color.BLACK);
        this.node = circle;
        birthTime = Instant.now();
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
        elapsedTime = Duration.between(this.birthTime, Ant_Simulator.scaledCurrent).toMillis()/1000;
        double probability = (double)elapsedTime/((double)lifespan * 100);
        if(elapsedTime != prevSec) {
            prevSec = elapsedTime;
            double d = Math.random();
            if (d < probability) {
                dead = true;
            }
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
     * Movement pattern for ant carrying food
     */
    public void moveWithFood(){
        int direction = findDirectionHome(); //Try to find the best way home
        if(direction == 4){ //If there isn't a best way home
            direction = findDirectionHomeNoPheromone(); //Find a way home based on distance
//            direction = findDirectionHomeSurrounding();
        }
        move(direction); //move towards the decided direction
        if((this.c.getX() + node.getTranslateX()) == this.c.getX()
                && (this.c.getY() + node.getTranslateY()) == this.c.getY()){
            ((Circle)this.node).setFill(Color.BLACK);
            ((Circle)this.node).setRadius(rad);
            hasFoodItem = false;
            knowFood = true;
        }
    }

    /**
     * Moves the ant to the given direction
     * @param direction direction to move towards
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
            visited.add(new Coordinate(this.getCurX(), this.getCurY()));
            if(visited.size() > 70) {
                visited.removeFirst();
            }
            behind = Math.abs(direction - 8);
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

    /**
     * Prints the direction representation of the integer
     * @param direction movement direction in int
     */
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

    /**
     * Find the direction during foraging, attempts to locate food pheromones
     * @return direction to move towards
     */
    public int findDirectionFood(){
        ArrayList<Pheromone_Data> surrounding = (Ant_Simulator.getSurrounding(this.getCurX(), this.getCurY()))
                .getInList();
        int moveX = 0;
        int moveY = 0;
        double max = 0;
        int direction = 4;
        ArrayList<Integer> d = prevDirection.getSide(prevDirection);
        for (int i  = 0; i < surrounding.size(); i++) {
            Pheromone_Data pd = surrounding.get(i);
            if(pd != null){
                if(i == 4){
                    pd.addHome();
                }
                else {
                    for (Ground_Data p : pd.getPheromones()) {
                        if (p.getName().equals("Obstacle")) {
                            blocked.add(i);
                        } else if (p.getName().equals("Food") && i != 4) {
                            Pheromone food = (Pheromone) p;
                            if (food.getValue() > 0) {
                                goingToFood();
                                if(!visited.contains(new Coordinate(food.getStartX(), food.getStartY()))) {
                                    direction = compareSurroundingFoodForage(pd, food, direction, i, moveX, moveY, max, d);
                                    if (direction == i) {
                                        moveX = surrounding.get(direction).getStartX();
                                        moveY = surrounding.get(direction).getStartY();
                                        max = food.getValue();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return direction;
    }

    /**
     * Compares the food pheromones of the current location to a surrounding location
     * @param pd pheromone data
     * @param p pheromone
     * @param curD current direction
     * @param i new direction
     * @param moveX x to increment
     * @param moveY y to increment
     * @param max current maximum pheromone
     * @param d frontward directions
     * @return direction with the greatest home pheromone
     */
    private int compareSurroundingFoodForage(Pheromone_Data pd, Pheromone p, int curD, int i, int moveX, int moveY,
                                             double max, ArrayList<Integer> d)
    {
        int direction = curD;
        //Calculate the euclidean distance of new and old direction to home
        double prev = euclideanDist(moveX, moveY, this.getStartX(), this.getStartY());
        double current = euclideanDist(p.getStartX(), p.getStartY(), this.getStartX(), this.getStartY());
        if (max == 0) { //if ant currently has no selected direction
            direction = i;
        }
        else if(p.getValue() > max && current >= prev){
            direction = i;
        }
        else if(p.getValue() == max) {
            if (current >= prev && i != behind) { //if new direction is further away from home
                direction = i;
            }
        }
        return direction;
    }

    /**
     * Find direction home with pheromones
     * @return direction to move towards
     */
    public int findDirectionHome(){
        /*
         * surrounding --> the data of the ants surroundings
         * moveX & moveY --> the current direction the ant is moving
         * max --> maximum home pheromone within the surroundings
         * direction --> current best direction the ant can take
         * defaultDirection --> a default direction an ant will take if it's unsure where to go
         * minDist --> current potential minimum distance to home in the surroundings
         * d --> The previous direction that the ant took
         * current --> the euclidean distance of the ant to hive
         */
        ArrayList<Pheromone_Data> surrounding = (Ant_Simulator.getSurrounding(this.getCurX(), this.getCurY()))
                .getInList();
        ArrayList<Pheromone_Data> checked = new ArrayList<>();
        int moveX = 0;
        int moveY = 0;
        double max = 0;
        int direction = 4;
        int defaultDirection = 4;
        double minDist = Integer.MAX_VALUE;
        ArrayList<Integer> front = prevDirection.getSide(prevDirection);
        double current = euclideanDist(this.getCurX(), this.getCurY(), this.getStartX(), this.getStartY());

        for (int i  = 0; i < surrounding.size(); i++) { //loop through all surrounding direction
            Pheromone_Data pd = surrounding.get(i); //obtain the current direction's pheromone data
            if (pd != null && i != behind) {
                if(i == 4){
                    pd.addFood();
                }
                else {
                    //calculate the euclidean distance of potential move
                    double potential = euclideanDist(pd.getStartX(), pd.getStartY(), this.getStartX(), this.getStartY());
                    //if the ant's current location is further from the hive than the current move
                    if (potential <= current) {
                        checked.add(pd);
                        if (minDist > potential) { //if the current minimum is bigger than the current direction
                            defaultDirection = i; //set the default direction to current move
                            minDist = potential; // sets the minimum distance to the current move distance
                        }
                        for (Ground_Data p : pd.getPheromones()) { //loop through the data on the ground
                            if (p.getName().equals("Obstacle")) {
                                blocked.add(i); //add to the list the direction that's been blocked
                            } else if (p.getName().equals("Home")) {
                                Pheromone home = (Pheromone) p;
                                if (home.getValue() > 0 && !visited.contains(new Coordinate(p.getStartX(), p.getStartY()))) {
                                    direction = compareSurroundingHome(pd, home, direction, i, moveX, moveY, max, front);
                                    if (direction == i) {
                                        moveX = surrounding.get(direction).getStartX();
                                        moveY = surrounding.get(direction).getStartY();
                                        max = home.getValue();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return direction;
    }

    /**
     * Find direction home without using the pheromones
     * @return direction to move towards
     */
    public int findDirectionHomeNoPheromone(){
        ArrayList<Pheromone_Data> surrounding = (Ant_Simulator.getSurrounding(this.getCurX(), this.getCurY()))
                .getInList();
        int direction = 4;
        double minDist = Integer.MAX_VALUE;
        double current = euclideanDist(this.getCurX(), this.getCurY(), this.getStartX(), this.getStartY());
        ArrayList<Integer> front = prevDirection.getSide(prevDirection);
        Direction back = Direction.values()[behind];
        ArrayList<Integer> backwards = back.getSurrounding(back);

        for (int i  = 0; i < surrounding.size(); i++) {
            Pheromone_Data pd = surrounding.get(i);
            if(pd != null) {
                for (Ground_Data p : pd.getPheromones()) {
                    if (!p.getName().equals("Obstacle")) {
                        Pheromone phe = (Pheromone) p;
                        if (phe.getValue() > 0) {
                            if(front.contains(i)){
                                direction = i;
                            }
                            if(!visited.contains(new Coordinate(p.getStartX(), p.getStartY()))) {
                                double potential = euclideanDist(pd.getStartX(), pd.getStartY(), this.getStartX(),
                                        this.getStartY());

                                if (potential <= current) {
                                    if (direction == 4) {
                                        direction = i;
                                    } else if (minDist > potential) {
                                        direction = i;
                                        minDist = potential;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return direction;
    }

    /**
     * Compares the home pheromones of the current location to a surrounding location
     * @param pd pheromone data
     * @param p pheromone
     * @param curD current direction
     * @param i new direction
     * @param moveX x to increment
     * @param moveY y to increment
     * @param max current maximum pheromone
     * @param d frontward directions
     * @return direction with the greatest home pheromone
     */
    private int compareSurroundingHome(Pheromone_Data pd, Pheromone p, int curD, int i, int moveX, int moveY, double max,
                                       ArrayList<Integer> d)
    {
        int direction = curD;
        if (p.getValue() > max) {
            direction = i;
        } else if (p.getValue() == max) {
            double prev = euclideanDist(pd.getStartX(), pd.getStartY(), moveX, moveY);
            double cur = euclideanDist(pd.getStartX(), pd.getStartY(), this.getCurX(), this.getCurY());
            if (cur > prev) {
                direction = i;
            }
        }
        return direction;
    }

    /**
     * Total random movement of ants
     */
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

    /**
     * Movement of ants prioritising moving frontwards
     */
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

    /**
     * Moves the ant NW
     */
    private void moveNW(){
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

    /**
     * Moves the ant N
     */
    private void moveN(){
        if(boundaryNegY()){
            moveS();
        }
        else {
            node.setTranslateY(getTranslateY() - velY);
            prevDirection = Direction.N;
        }
    }

    /**
     * Moves the ant NE
     */
    private void moveNE(){
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

    /**
     * Moves the ant W
     */
    private void moveW(){
        if(boundaryNegX()){
            moveE();
        }
        else {
            node.setTranslateX(getTranslateX() - velX);
            prevDirection = Direction.W;
        }
    }

    /**
     * Moves the ant E
     */
    private void moveE(){
        if(boundaryX()){
            moveW();
        }
        else {
            node.setTranslateX(getTranslateX() + velX);
            prevDirection = Direction.E;
        }
    }

    /**
     * Moves the ant SW
     */
    private void moveSW(){
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

    /**
     * Moves the ant S
     */
    private void  moveS(){
        if(boundaryY()){
            moveN();
        }
        else {
            node.setTranslateY(getTranslateY() + velY);
            prevDirection = Direction.S;
        }
    }

    /**
     * Moves the ant SE
     */
    private void  moveSE(){
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

    /**
     * Check if the ants collided with the hive
     * @param other
     * @return
     */
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
        visited.clear();
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

    public boolean isFollowingFood(){
        return this.followFood;
    }

    public void goingToFood(){
        this.followFood = true;
    }

    @Override
    public String toString(){
        String s = "";
        s += "Birth time         :" + birthTime + "\n";
        s += "Carrying food      :" + hasFoodItem + "\n";
        s += "Following food     :" + knowFood + "\n";
        s += "Blocked directions :" + blocked.toString() + "\n";
        s += "Previous direction :" + prevDirection.toString() + "\n";

        return s;
    }
}
