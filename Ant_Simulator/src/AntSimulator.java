import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.util.Random;

public class AntSimulator extends Simulator{

    private int numAnt;
    private int homeX, homeY;

    public AntSimulator(int fps){
        super(fps);
    }

    @Override
    public void initialize(final Stage primaryStage){
        primaryStage.setTitle("Ant simulator");
        this.root = new Group();
        this.scene = new Scene(this.root,WIDTH,HEIGHT);
        primaryStage.setScene(this.scene);

        Random r = new Random();
        this.homeX = r.nextInt(WIDTH);
        this.homeY = r.nextInt(HEIGHT);
        generateHive();
        generateAnts(500, this.homeX, this.homeY);
        generateFood();

        final Timeline loop = Simulator.timeline;
    }

    @Override
    public boolean handleCollision(Sprite s, Sprite c){
        if(s instanceof Ant){
            Ant a = (Ant)s;
            if(a.collision(c)){
                    ((Circle)s.getNode()).setRadius(3.0);
                    ((Circle)s.getNode()).setFill(Color.RED);
                    ((Ant)s).obtainedFood();

                    return true;
                }
            if(a.hasFood()){
                Circle circle = new Circle();
                circle.setCenterX(a.getStartX() + a.getX());
                circle.setCenterY(a.getStartY() + a.getY());
                circle.setRadius(Ant.rad);
                circle.setFill(Color.YELLOW);

                this.root.getChildren().add(circle);
            }
        }
        return false;
    }

    public void generateAnts(int amt, int x, int y){
        Ant ant;
        for(int i = 0; i < amt; i++){
            ant = new Ant(x, y,5, 5);
            this.handler.addObject(ant);
            this.root.getChildren().add(0, ant.getNode());
        }
    }

    public void generateHive(){
        Random r = new Random();
        Hive hive = new Hive(this.homeX, this.homeY);
        this.handler.addObject(hive);
        this.root.getChildren().add(0, hive.getNode());
    }

    public void generateFood(){
        Random r = new Random();
        Food food = new Food(r.nextInt(WIDTH), r.nextInt(HEIGHT));

        this.handler.addObject(food);
        this.root.getChildren().add(0, food.getNode());
    }
}
