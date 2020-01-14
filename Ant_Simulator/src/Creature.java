import javafx.animation.AnimationTimer;

import java.time.Instant;

public abstract class Creature extends Sprite{
    protected boolean dead = false;
    protected Instant birthTime;
    protected long lifespan;
    protected long elapsedTime = 0;
    protected long prevSec = 0;
    protected AnimationTimer timer;
    protected static final double rad = 1.0;

    public Creature(int x, int y, int velX, int velY) {
        super(x, y, velX, velY);
    }

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public Instant getBirthTime() {
        return birthTime;
    }

    public void setBirthTime() {
        this.birthTime = Instant.now();
    }

    public static double getRad() {
        return rad;
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(long elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public long getPrevSec() {
        return prevSec;
    }

    public void setPrevSec(long prevSec) {
        this.prevSec = prevSec;
    }

    public AnimationTimer getTimer() {
        return timer;
    }

    public void setTimer(AnimationTimer timer) {
        this.timer = timer;
    }

    public long getLifespan() {
        return lifespan;
    }

    public void setLifespan(long lifespan) {
        this.lifespan = lifespan*24*60*60;
    }
}
