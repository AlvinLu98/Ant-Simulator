import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Pheromone extends Sprite{
    private String name;
    private int r, g, b;
    private double evaporation;
    private double value;
    private double increment;
    private int reward;

    public Pheromone(int x, int y, int width, int height, String name, double increment, int r, int g, int b, int reward) {
        super(x,y);
        this.name = name;
        this.r = r;
        this.g = g;
        this.b = b;
        this.value = 0;
        this.increment = increment;
        this.evaporation = 0.99; //TODO figure out what this means
        this.reward = reward;

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
        Rectangle r = (Rectangle)this.getNode();
        Color c = (Color)r.getFill();
        if(c.getOpacity() != 0.0) {
            this.evaporate(c);
            Color newC = new Color(c.getRed(), c.getGreen(), c.getBlue(), this.value);
            r.setFill(newC);
        }
    }

    public void addValue(){
        this.value += increment;
    }

    public void evaporate(Color c){
        this.value *= evaporation;
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
        return this.value*this.reward;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getIncrement() {
        return increment;
    }

    public void setIncrement(double increment) {
        this.increment = increment;
    }
}
