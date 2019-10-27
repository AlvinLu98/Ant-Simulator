import javafx.scene.Node;
import javafx.scene.shape.Circle;

public abstract class Sprite {

    protected Node node; //Node containing the shape definition of the sprite
    protected Coordinate c; //Starting location of the sprite
    protected int velX, velY; //speed of the sprite

    /**
     * Constrcutor for Sprites that doesn't move
     * @param x location of x-axis
     * @param y location of y-axis
     */
    public Sprite(int x, int y){
        c = new Coordinate(x,y);
        this.velX = 0;
        this.velY = 0;
    }

    /**
     * Constructor for moving sprites
     * @param x location of x-axis
     * @param y location of y-axis
     * @param velX speed of sprite in x
     * @param velY speed of sprite in y
     */
    public Sprite(int x, int y, int velX, int velY) {
        c = new Coordinate(x,y);
        this.velX = velX;
        this.velY = velY;
    }

    /**
     * Abstract method
     * Method that updates the sprites with each tick
     */
    public abstract void tick();

    /**
     * Check if this sprite is colliding with another
     * @param other Sprite to check collision with
     * @return true if sprites collided
     */
    public  boolean collision(Sprite other){return false;}

    /**
     * Check if sprite is moving outside of screen in x-axis
     */
    public void boundaryX(){
        if((c.getX() + node.getTranslateX()) < 0
                || c.getX() + node.getTranslateX() > (AntSimulator.WIDTH - node.getBoundsInParent().getWidth()) ){
            this.velX *= -1;
        }
    }

    /**
     * Check if sprite is moving outside of screen in y-axis
     */
    public void boundaryY(){
        if((this.c.getY() + node.getTranslateY()) < 0
                || (this.c.getY() + node.getTranslateY()) >
                (AntSimulator.HEIGHT - node.getBoundsInParent().getHeight()) ){
            this.velY *= -1;
        }
    }

    /**
     * Get the amount x-axis was translated from the starting point
     * @return translated value of x-axis in double
     */
    public double getTranslateX() {
        return node.getTranslateX();
    }

    /**
     * Sets the translated value of x-axis to given value
     * @param x double
     */
    public void setTranslateX(double x) {
        node.setTranslateX(x);
    }

    /**
     * get the starting x-axis position of sprite
     * @return double
     */
    public double getStartX(){return this.c.getX();}

    /**
     *  Get the amount y-axis was translated from the starting point
     * @return translated value of y-axis in double
     */
    public double getTranslateY() {
        return node.getTranslateY();
    }

    /**
     * Sets the translated value of y-axis to given value
     * @param y double
     */
    public void setTranslateY(double y) {
        node.setTranslateY(y);
    }

    /**
     * get the starting y-axis position of sprite
     * @return double
     */
    public double getStartY(){return this.c.getY();}

    /**
     * Get speed of sprite in the x-axis
     * @return int
     */
    public int getVelX() {
        return velX;
    }

    /**
     * Set speed of sprite in the x-axis
     * @param velX speed in int
     */
    public void setVelX(int velX) {
        this.velX = velX;
    }

    /**
     * Get speed of sprite in the y-axis
     * @return int
     */
    public int getVelY() {
        return velY;
    }

    /**
     * Set speed of sprite in the y-axis
     * @param velY speed in int
     */
    public void setVelY(int velY) {
        this.velY = velY;
    }

    /**
     * get the Node of the Sprite
     * @return Node
     */
    public Node getNode() {
        return node;
    }

    /**
     * Set the node of the Sprite
     * @param node Node
     */
    public void setNode(Node node) {
        this.node = node;
    }

    /**
     * Get the bounds of the sprite
     * @param rad
     * @return
     */
    public Circle getBounds(double rad){
        return new Circle((this.c.getX()+ node.getTranslateX()), (this.c.getY()+node.getTranslateY()), rad);
    }
}
