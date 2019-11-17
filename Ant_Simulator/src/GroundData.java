import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class GroundData extends Sprite{
    protected String name;
    protected int r,g,b;



    public GroundData(String name, int x, int y, int width, int height, int r, int g, int b) {
        super(x, y);
        this.name = name;
        this.r = r;
        this.g = g;
        this.b = b;

        Rectangle rec = drawRectangle(x, y, width, height);
        Color c = Color.rgb( r, g, b, 0);
        rec.setFill(c);
        this.node = rec;
    }

    public GroundData(String name, int x, int y, int width, int height, int r, int g, int b, double opacity) {
        super(x, y);
        this.name = name;
        this.r = r;
        this.g = g;
        this.b = b;

        Rectangle rec = drawRectangle(x, y, width, height);
        Color c = Color.rgb( r, g, b, opacity);
        rec.setFill(c);
        this.node = rec;
    }

    @Override
    public void tick() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRed() {
        return r;
    }

    public void setRed(int r) {
        this.r = r;
    }

    public int getGreen() {
        return g;
    }

    public void setGreen(int g) {
        this.g = g;
    }

    public int getBlue() {
        return b;
    }

    public void setBlue(int b) {
        this.b = b;
    }
}
