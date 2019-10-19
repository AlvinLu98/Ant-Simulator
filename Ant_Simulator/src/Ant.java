import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.Random;

public class Ant extends Sprite {
    private boolean hasFoodItem;
    public static final double rad = 1.0;

    public Ant(int x, int y, int velX, int velY) {
        super(x,y,velX, velY, ID.Ant);
        this.hasFoodItem = false;

        Circle circle = new Circle();
        circle.setCenterX(x);
        circle.setCenterY(y);
        circle.setRadius(rad);

        this.node = circle;
    }

    @Override
    public void tick() {
        if(hasFoodItem){
            moveWithFood();
        }
        else{
            moveNoFood();
        }
    }

    public void moveNoFood(){
        Random r = new Random();
        int x = r.nextInt()%3;
        if(x == 2){
            node.setTranslateX(getX() + this.velX);
            boundaryX();
        }
        else if(x == 1){
            boundaryX();
            node.setTranslateX(getX() - this.velX);
        }

        int y = r.nextInt()%3;

        if(y == 2){
            node.setTranslateY(getY() + this.velY);
            boundaryY();
        }

        else if(y == 1){
            boundaryY();
            node.setTranslateY(getY() - this.velY);
        }
    }

    public void moveWithFood(){
        if(velX < 0){
            velX *= -1;
        }
        if(velY < 0){
            velY *= -1;
        }

        if((this.x + this.node.getTranslateX()) < this.x){
            node.setTranslateX(getX() + velX);
        }
        else if ((this.x + this.node.getTranslateX()) > this.x){
            node.setTranslateX(getX() - velX);
        }

        if((this.y + this.node.getTranslateY()) < this.y){
            node.setTranslateY(getY() + velY);
        }
        else if((this.y + this.node.getTranslateY()) > this.y){
            node.setTranslateY(getY() - velY);
        }

        if((this.x + node.getTranslateX()) == this.x
                && (this.y + node.getTranslateY()) == this.y){
            ((Circle)this.node).setFill(Color.GREENYELLOW);
            hasFoodItem = false;
        }
    }

    public void boundaryX(){
        if((this.x + node.getTranslateX()) < 0
                || this.x + node.getTranslateX() > (AntSimulator.WIDTH - node.getBoundsInParent().getWidth()) ){
            this.velX *= -1;
        }
    }

    public void boundaryY(){
        if((this.y + node.getTranslateY()) < 0
                || (this.y + node.getTranslateY()) > (AntSimulator.HEIGHT - node.getBoundsInParent().getHeight()) ){
            this.velY *= -1;
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

    public boolean antCollision(Ant other){
        Circle thisAnt = this.getBounds(rad);
        Circle otherAnt = other.getBounds(rad);
        if(thisAnt.getBoundsInParent().intersects(otherAnt.getBoundsInParent())){
            return true;
        }
        return false;
    }

    public boolean foodCollision(Food other){
        Circle thisAnt = this.getBounds(rad);
        Circle food = other.getBounds(Food.rad);
        if(thisAnt.getBoundsInParent().intersects(food.getBoundsInParent())){
            return true;
        }
        return false;
    }

    public void obtainedFood(){
        this.hasFoodItem = true;
    }

    public boolean hasFood(){
        return this.hasFoodItem;
    }
}
