import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Hive extends Sprite {

    public static final double rad = 2.5;

    public Hive(int x, int y) {
        super(x, y, ID.Hive);

        Circle circle = new Circle();
        circle.setCenterX(x);
        circle.setCenterY(y);
        circle.setRadius(rad);
        circle.setFill(Color.GREENYELLOW);

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
