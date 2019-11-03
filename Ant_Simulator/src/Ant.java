import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.Random;

public class Ant extends Sprite {
    private boolean hasFoodItem; //if ants has food
    private boolean knowFood;
    private boolean foundFood;

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

        Circle circle = drawCircle(x, y, this.rad);
        circle.setFill(Color.BLACK);
        this.node = circle;
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
    }

    /**
     * Movement when the ant is not holding food
     */
    public void moveNoFood(){
        ArrayList<PheromoneData> surrounding = (AntSimulator.getSurrounding(this.getCurX(), this.getCurY()))
                .getInList();
        int moveX = 0;
        int moveY = 0;
        double max = 0;
        int direction = 4;
        for (int i  = 0; i < surrounding.size(); i++) {
            PheromoneData pd = surrounding.get(i);
            if(pd != null){
                for (Pheromone p : pd.getPheromones()) {
                    if (p.getName().equals("Food") && p.getValue() > 0) {
                        if (max == 0) {
                            direction = i;
                            moveX = pd.getStartX();
                            moveY = pd.getStartY();
                            max = p.getValue() * 10;
                        }
                        else if(p.getValue() * 10 >= max) {
                            if(p.getValue() * 10 == max){
                                double prev = euclideanDist(this.getStartX(), this.getStartY(), moveX, moveY);
                                double current = euclideanDist(this.getStartX(), this.getStartY(),
                                        pd.getStartX(), pd.getStartY());
                                if(current > prev){
                                    direction = i;
                                    moveX = pd.getStartX();
                                    moveY = pd.getStartY();
                                    max = p.getValue() * 10;
                                }
                            }
                        }
                    }
                }
            }
        }
        if(direction != 4){
            switch(direction){
                case 0:
                    moveNW();
                    break;
                case 1:
                    moveN();
                    break;
                case 2:
                    moveNE();
                    break;
                case 3:
                    moveW();
                    break;
                case 5:
                    moveE();
                    break;
                case 6:
                    moveSW();
                    break;
                case 7:
                    moveS();
                    break;
                case 8:
                    moveSE();
                    break;
                default:
                    randomMovement();
                    break;
            }
        }
        else{
            randomMovement();
        }
    }

    /**
     * Movement when ant is holding food
     */
    public void moveWithFood(){
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


    public void randomMovement(){
        Random r = new Random();
        int x = r.nextInt()%3;
        if(x == 2){
            node.setTranslateX(getTranslateX() + this.velX);
            boundaryX();
        }
        else if(x == 1){
            boundaryX();
            node.setTranslateX(getTranslateX() - this.velX);
        }

        int y = r.nextInt()%3;

        if(y == 2){
            node.setTranslateY(getTranslateY() + this.velY);
            boundaryY();
        }

        else if(y == 1){
            boundaryY();
            node.setTranslateY(getTranslateY() - this.velY);
        }
    }

    public void moveNW(){
        node.setTranslateX(getTranslateX() - velX);
        boundaryX();
        node.setTranslateY(getTranslateY() - velY);
        boundaryY();
    }

    public void moveN(){
        node.setTranslateY(getTranslateY() - velY);
        boundaryY();
    }

    public void moveNE(){
        node.setTranslateX(getTranslateX() + velX);
        boundaryX();
        node.setTranslateY(getTranslateY() - velY);
        boundaryY();
    }

    public void moveW(){
        node.setTranslateX(getTranslateX() - velX);
        boundaryX();
    }

    public void moveE(){
        node.setTranslateX(getTranslateX() + velX);
        boundaryX();
    }

    public void moveSW(){
        node.setTranslateX(getTranslateX() - velX);
        boundaryX();
        node.setTranslateY(getTranslateY() + velY);
        boundaryY();
    }

    public void  moveS(){
        node.setTranslateY(getTranslateY() + velY);
        boundaryY();
    }

    public void  moveSE(){
        node.setTranslateX(getTranslateX() + velX);
        boundaryX();
        node.setTranslateY(getTranslateY() + velY);
        boundaryY();
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
}
