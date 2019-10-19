import java.util.LinkedList;

public class Handler {
    private LinkedList<Sprite> objects = new LinkedList<>();

    private LinkedList<Sprite> collisionCheck = new LinkedList<>();

    private LinkedList<Sprite> cleanUp = new LinkedList<>();

    public Handler(){}

    public void tick(){
        for(Sprite o:objects){
            o.tick();
        }
    }

    public void addObject(Sprite o){
        this.objects.add(o);
    }

    public void removeObject(Sprite o){
        this.objects.remove(o);
    }

    public void cleanUp(){
        this.objects.removeAll(this.cleanUp);
        this.cleanUp.clear();
    }

    public LinkedList<Sprite> getObjects() {
        return objects;
    }

    public void setObjects(LinkedList<Sprite> objects) {
        this.objects = objects;
    }

    public LinkedList<Sprite> getCollisionCheck() {
        return collisionCheck;
    }

    public void setCollisionCheck(LinkedList<Sprite> collisionCheck) {
        this.collisionCheck = collisionCheck;
    }

    public void resetCollisionCheck(){
        this.collisionCheck.clear();
        this.collisionCheck.addAll(this.objects);
    }

    public LinkedList<Sprite> getCleanUp() {
        return cleanUp;
    }

    public void setCleanUp(LinkedList<Sprite> cleanUp) {
        this.cleanUp = cleanUp;
    }
}
