import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.Random;

public class Ant extends Sprite {
    private boolean hasFoodItem; //if ants has food
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
        super(x,y,velX, velY, ID.Ant);
        this.hasFoodItem = false;
        this.foundFood = false;

        Circle circle = new Circle();
        circle.setCenterX(x);
        circle.setCenterY(y);
        circle.setRadius(rad);

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
        else{
            moveNoFood();
        }
    }

    /**
     * Movement when the ant is not holding food
     */
    public void moveNoFood(){
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

        if((this.x + this.node.getTranslateX()) < this.x){
            node.setTranslateX(getTranslateX() + velX);
        }
        else if ((this.x + this.node.getTranslateX()) > this.x){
            node.setTranslateX(getTranslateX() - velX);
        }

        if((this.y + this.node.getTranslateY()) < this.y){
            node.setTranslateY(getTranslateY() + velY);
        }
        else if((this.y + this.node.getTranslateY()) > this.y){
            node.setTranslateY(getTranslateY() - velY);
        }

        if((this.x + node.getTranslateX()) == this.x
                && (this.y + node.getTranslateY()) == this.y){
            ((Circle)this.node).setFill(Color.DARKGREEN);
            hasFoodItem = false;
        }
    }

    /**
     * https://stackoverflow.com/questions/15013913/checking-collision-of-shapes-with-javafx
     * @param other
     * @return
     */
    @Override
    public boolean collision(Sprite other) {
        /*if(other instanceof Ant){
            return antCollision((Ant)other);
        }*/
        if(other instanceof Food){
            return foodCollision((Food)other);
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

    /**
     * Set hasFoodItem to true
     */
    public void obtainedFood(){
        this.hasFoodItem = true;
    }

    /**
     * Check if the ant has food
     * @return true if ant hasFood
     */
    public boolean hasFood(){
        return this.hasFoodItem;
    }
}
