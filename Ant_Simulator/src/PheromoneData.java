import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.LinkedList;

//create a square according to size?
public class PheromoneData extends Sprite{
    private LinkedList<GroundData> pheromones;


    public PheromoneData(int x, int y, int width, int height) {
        super(x,y);
        this.pheromones = new LinkedList<>();
    }

    @Override
    public void tick() {

    }

    public void addData(GroundData p){
        this.pheromones.add(p);
    }

    public void addFood(){
        for(GroundData p: this.pheromones){
            if(p.getName().equals("Food")){
                Pheromone food = (Pheromone)p;
                Rectangle r = (Rectangle)p.getNode();
                Color c = (Color)r.getFill();
                if(c.getOpacity() <= 0.5){
                    food.addValue();
                    Color newC = new Color(c.getRed(), c.getGreen(), c.getBlue(), c.getOpacity() + 0.5);
                    r.setFill(newC);
                }

            }
        }
    }

    public void addHome(){
        for(GroundData p: this.pheromones){
            if(p.getName().equals("Home")){
                Pheromone home = (Pheromone)p;
                Rectangle r = (Rectangle)p.getNode();
                Color c = (Color)r.getFill();
                if(c.getOpacity() <= 0.3){
                    home.addValue();
                    Color newC = new Color(c.getRed(), c.getGreen(), c.getBlue(), c.getOpacity() + 0.05);
                    r.setFill(newC);
                }
            }
        }
    }

    public LinkedList<GroundData> getPheromones() {
        return pheromones;
    }
}
