import java.util.LinkedList;

public class Handler {
    private LinkedList<Sprite> objects = new LinkedList<>(); //list containing all sprite objects

    private LinkedList<Sprite> collisionCheck = new LinkedList<>(); //list containing objects to check for collision

    private LinkedList<Sprite> cleanUp = new LinkedList<>(); //list of sprites to remove from scene

    /**
     * Default constructor
     */
    public Handler(){}

    /**
     * Generates a Handler given the objects
     * @param objects
     */
    public Handler(LinkedList<Sprite> objects){
        this.objects = objects;
    }

    /**
     * Method to update the sprites each frame
     */
    public void tick(){
        for(Sprite o:objects){
            o.tick();
            if(o instanceof Ant){
                if(((Ant)o).isDead()){
                    addToRemove(o);
                    Simulator.rootNode.getChildren().remove(o.node);
                    Ant_Simulator.modifyAntPop(-1);
                }
            }
        }
    }

    /**
     * adds the given sprite to the list
     * @param o Sprite object to add
     */
    public void addObject(Sprite o){
        this.objects.add(o);
    }

    /**
     * Remove a sprite object from list
     * @param o Sprite object to remove
     */
    public void removeObject(Sprite o){
        this.objects.remove(o);
    }

    public void addToRemove(Sprite o){this.cleanUp.add(o);}

    /**
     * Removes all Sprite object in cleanUp list
     */
    public void cleanUp(){
        this.objects.removeAll(this.cleanUp);
        this.cleanUp.clear();
    }

    /**
     * Gets the list of objects
     * @return LinkedList containing Sprite objects
     */
    public LinkedList<Sprite> getObjects() {
        return objects;
    }

    /**
     * Set the sprite list to the given list
     * @param objects list of Sprite objects
     */
    public void setObjects(LinkedList<Sprite> objects) {
        this.objects = objects;
    }

    /**
     * get the list of sprite object to check against collision
     * @return linked list containing sprite objects
     */
    public LinkedList<Sprite> getCollisionCheck() {
        return collisionCheck;
    }

    /**
     * Resets the collision check list
     * Adds all current objects into the collision check list
     */
    public void resetCollisionCheck(){
        this.collisionCheck.clear();
        this.collisionCheck.addAll(this.objects);
    }

    public LinkedList<Sprite> getCleanUp() {
        return cleanUp;
    }

    public void emptySprites(){
        this.objects.clear();
    }
}
