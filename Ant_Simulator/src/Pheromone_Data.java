import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.LinkedList;

//create a square according to size?
public class Pheromone_Data extends Sprite{
    private LinkedList<Ground_Data> pheromones;


    public Pheromone_Data(int x, int y, int width, int height) {
        super(x,y);
        this.pheromones = new LinkedList<>();
    }

    @Override
    public void tick() {

    }

    public void addData(Ground_Data p){
        this.pheromones.add(p);
    }

    public void addFood(){
        for(Ground_Data p: this.pheromones){
            if(p.getName().equals("Food")){
                Pheromone food = (Pheromone)p;
                Rectangle r = (Rectangle)p.getNode();
                Color c = (Color)r.getFill();
                if(c.getOpacity() <= 0.7){
                    food.addValue();
                    Color newC = new Color(c.getRed(), c.getGreen(), c.getBlue(), c.getOpacity() + food.getIncrement());
                    r.setFill(newC);
                }

            }
        }
    }

    public void addHome(){
        for(Ground_Data p: this.pheromones){
            if(p.getName().equals("Home")){
                Pheromone home = (Pheromone)p;
                Rectangle r = (Rectangle)p.getNode();
                Color c = (Color)r.getFill();
                if(c.getOpacity() <= 0.5){
                    home.addValue();
                    Color newC = new Color(c.getRed(), c.getGreen(), c.getBlue(), c.getOpacity() + home.getIncrement());
                    r.setFill(newC);
                }
            }
        }
    }

    public LinkedList<Ground_Data> getPheromones() {
        return pheromones;
    }
}
