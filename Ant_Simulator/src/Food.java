import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Food extends Sprite{

    public static final double rad = 2.5;

    public Food(int x, int y) {
        super(x, y, ID.Food);

        Circle circle = new Circle();
        circle.setCenterX(x);
        circle.setCenterY(y);
        circle.setRadius(rad);
        circle.setFill(Color.RED);

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
