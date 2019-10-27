import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Hive extends Sprite {

    public static final double rad = 3;

    /**
     * Constructor for Hive
     * @param x location in x-axis
     * @param y location in y-axis
     */
    public Hive(int x, int y) {
        super(x, y);

        Circle circle = new Circle();
        circle.setCenterX(x);
        circle.setCenterY(y);
        circle.setRadius(rad);
        circle.setFill(Color.BROWN);

        this.node = circle;
    }

    @Override
    public void tick() {

    }

    @Override
    public boolean collision(Sprite other) {
        return false;
    }
}
