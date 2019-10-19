import javafx.scene.Node;
import javafx.scene.shape.Circle;

public abstract class Sprite {

    protected Node node;
    protected int x, y;
    protected int velX, velY;
    protected ID id;

    public Sprite(int x, int y, ID id){
        this.x = x;
        this.y = y;
        this.velX = 0;
        this.velY = 0;
        this.id = id;
    }

    public Sprite(int x, int y, int velX, int velY, ID id) {
        this.x = x;
        this.y = y;
        this.velX = velX;
        this.velY = velY;
        this.id = id;
    }

    public abstract void tick();

    public  boolean collision(Sprite other){return false;}

    public double getX() {
        return node.getTranslateX();
    }

    public void setX(double x) {
        node.setTranslateX(x);
    }

    public double getStartX(){return this.x;}

    public double getY() {
        return node.getTranslateY();
    }

    public void setY(double y) {
        node.setTranslateY(y);
    }

    public double getStartY(){return this.y;}

    public int getVelX() {
        return velX;
    }

    public void setVelX(int velX) {
        this.velX = velX;
    }

    public int getVelY() {
        return velY;
    }

    public void setVelY(int velY) {
        this.velY = velY;
    }

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public Circle getBounds(double rad){
        return new Circle((x+ node.getTranslateX()), (y+node.getTranslateY()), rad);
    }
}
