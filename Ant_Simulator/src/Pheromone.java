public class Pheromone extends Sprite{
    private String name;
    private int r, g, b;
    private double evaporation;

    //TODO while drawing rec, center must be plus 2 to accommodate radius
    public Pheromone(int x, int y, int velX, int velY) {
        super(x, y);
    }

    public Pheromone(int x, int y , int velX, int velY, String name, int r, int g, int b) {
        super(x, y);
        this.name = name;
        this.r = r;
        this.g = g;
        this.b = b;
        this.evaporation = 0.9; //TODO figure out what this means
    }

    public Pheromone(int x, int y , int velX, int velY, String name, int r, int g, int b, double evaporation) {
        super(x, y);
        this.name = name;
        this.r = r;
        this.g = g;
        this.b = b;
        this.evaporation = evaporation;
    }

    @Override
    public void tick() {

    }

    @Override
    public String toString() {
        return "Pheromone: "  + this.name + '\n';
    }
}
