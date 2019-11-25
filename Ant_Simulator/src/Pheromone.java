import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Pheromone extends GroundData{
    private double evaporation;
    private double value;
    private double increment;
    private int reward;
    private int extRew;

    public Pheromone(int x, int y, int width, int height, String name, double increment, int r, int g, int b, int reward,
                     double evaporation) {
        super(name, x,y, width, height, r, g, b);
        this.r = r;
        this.g = g;
        this.b = b;
        this.value = 0;
        this.increment = increment;
        this.evaporation = evaporation;
        this.reward = reward;
    }

    public Pheromone(int x, int y, int width, int height, String name, int r, int g, int b, double evaporation) {
        super(name, x,y, width, height, r, g, b);
        this.r = r;
        this.g = g;
        this.b = b;
        this.value = 0;
        this.evaporation = evaporation;
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
        if(c.getOpacity() < 0.001){
            this.value = 0;
            Color newC = new Color(c.getRed(), c.getGreen(), c.getBlue(), this.value);
            r.setFill(newC);
        }
    }

    public void addValue(){
        this.value += increment;
    }

    private void evaporate(Color c){
        this.value *= evaporation;
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
