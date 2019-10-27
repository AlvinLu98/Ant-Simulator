import java.util.LinkedList;

//create a square according to size?
public class PheromoneData extends Sprite{
    private LinkedList<Pheromone> pheromones;
    //TODO use a changed boolean to speed up checking?


    public PheromoneData(int x, int y) {
        super(x,y);
        this.pheromones = new LinkedList<>();
    }

    @Override
    public void tick() {
        for(Pheromone p: pheromones){
            p.tick();
        }
    }

    public void addPheromone(Pheromone p){
        this.pheromones.add(p);
    }

}
