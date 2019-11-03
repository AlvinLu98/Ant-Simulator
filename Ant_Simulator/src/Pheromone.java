import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Pheromone extends Sprite{
    private String name;
    private int r, g, b;
    private double evaporation;
    private double value;

    public Pheromone(int x, int y, int width, int height, String name, int r, int g, int b) {
        super(x,y);
        this.name = name;
        this.r = r;
        this.g = g;
        this.b = b;
        this.value = 0;
        this.evaporation = 0.9; //TODO figure out what this means

        Rectangle rec = drawRectangle(x, y, width, height);
        Color c = Color.rgb( r, g, b, 0);
        rec.setFill(c);
        this.node = rec;
    }

    public Pheromone(int x, int y, int width, int height, String name, int r, int g, int b, double evaporation) {
        super(x,y);
        this.name = name;
        this.r = r;
        this.g = g;
        this.b = b;
        this.value = 0;
        this.evaporation = evaporation;

        Rectangle rec = drawRectangle(x, y, width, height);
        Color c = new Color(r, g, b, 0);
        rec.setFill(c);
        this.node = rec;
    }

    @Override
    public String toString() {
        return "Pheromone: "  + this.name + '\n';
    }

    @Override
    public void tick() {

    }

    public void addValue(double i){
        this.value += i;
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

    public double getEvaporation() {
        return evaporation;
    }

    public void setEvaporation(double evaporation) {
        this.evaporation = evaporation;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
