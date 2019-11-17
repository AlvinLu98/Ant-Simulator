public class Obstacle extends GroundData {

    private int height;

    public Obstacle(int x, int y, int width, int height, int r, int g, int b) {
        super("Obstacle", x, y, width, height, r, g, b, 1.0);
    }

    public Obstacle(int x, int y, int width, int height, int r, int g, int b, double opacity, int z) {
        super("Obstacle", x, y, width, height, r, g, b, opacity);
    }

    @Override
    public void tick() {

    }
}
