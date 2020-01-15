import java.util.LinkedList;

public class Handler_Thread extends Thread{
    private LinkedList<Sprite> objects = new LinkedList<>(); //list containing all sprite objects

    private LinkedList<Sprite> collisionCheck = new LinkedList<>(); //list containing objects to check for collision

    private LinkedList<Sprite> cleanUp = new LinkedList<>(); //list of sprites to remove from scene

    protected int nk;

    public Handler_Thread(int nk, ThreadGroup tg){
        super(tg, "thread");
        this.nk = nk;
    }

    @Override
    public void run(){

    }
}
