import bagel.Image;
import bagel.util.Point;

import java.sql.Time;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public abstract class DemonEnemy extends Entity implements Moveable, Invincible{
    protected final double INVINCIBLE_TIME = 3000;
    protected final int DEMON_DAMAGE = 10;
    protected final int DEMON_HEALTH = 40;
    protected final int MIN_HEALTH = 0;
    private final static double MIN_SPEED = 0.2;
    private final static double MAX_SPEED = 0.7;
    protected final int HEALTHBAR_FONT = 15;

    public abstract boolean attack(Player player);

    // generates a random direction when called
    enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT;
        public static Direction setRandomDirection() {
            Random random = new Random();
            return values()[random.nextInt(values().length)];
        }
    }


    public double setRandomSpeed() {
        Random r = new Random();
        double randomSpeed = MIN_SPEED + (MAX_SPEED - MIN_SPEED) * r.nextDouble();
        return randomSpeed;
    }


    abstract void update(LevelManager gameObject);
    abstract void updateTimescaleSpeed();
    abstract void render();
}
