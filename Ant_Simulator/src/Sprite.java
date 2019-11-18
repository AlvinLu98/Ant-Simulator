import javafx.scene.Node;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;


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
    public boolean boundaryX(){
        //            this.velX *= -1;
        return (c.getX() + node.getTranslateX() + velX) < 0
                || c.getX() + node.getTranslateX() + velX > (AntSimulator.WIDTH);
    }

    public boolean boundaryNegX(){
        //            this.velX *= -1;
        return (c.getX() + node.getTranslateX() - velX) < 0
                || c.getX() + node.getTranslateX() - velX > (AntSimulator.WIDTH);
    }

    /**
     * Check if sprite is moving outside of screen in y-axis
     */
    public boolean boundaryY(){
        //            this.velY *= -1;
        return (this.c.getY() + node.getTranslateY() + velY) < 0
                || (this.c.getY() + node.getTranslateY() + velY) >
                (AntSimulator.HEIGHT);
    }

    public boolean boundaryNegY(){
        //            this.velY *= -1;
        return (this.c.getY() + node.getTranslateY() - velY) < 0
                || (this.c.getY() + node.getTranslateY() - velY) >
                (AntSimulator.HEIGHT);
    }

    public static Rectangle drawRectangle(int x, int y, int width, int height){
        Rectangle rec = new Rectangle(x, y, width, height);
        return rec;
    }

    public static Circle drawCircle(int x, int y, double rad){
        Circle c = new Circle();
        c.setCenterX(x);
        c.setCenterY(y);
        c.setRadius(rad);
        return c;
    }

    public static double euclideanDist(int x1, int y1, int x2, int y2){
        double distance = (y2 - y1)*(y2 - y1) + (x2 - x1)*(x2 - x1);
//        System.out.println(x2 +"-"+x1+"^2 " + y2+"-"+y1+"^2 = "+ distance);
        return Math.sqrt(distance);
    }

    /**
     * Get the bounds of the sprite
     * @param rad
     * @return
     */
    public Circle getBounds(double rad){
        return new Circle((this.c.getX()+ node.getTranslateX()), (this.c.getY()+node.getTranslateY()), rad);
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
    public int getStartX(){return this.c.getX();}

    public int getCurX(){return this.c.getX() + (int)node.getTranslateX();}

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
    public int getStartY(){return this.c.getY();}

    public int getCurY(){return this.c.getY() + (int)node.getTranslateY();}

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
}
