import bagel.Image;
import bagel.util.Point;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public abstract class DemonEnemy extends Entity implements Moveable{
    private final double INVINCIBLE_TIME = 3000;
    private final int DEMON_DAMAGE = 10;
    private final static double MIN_SPEED = 0.2;
    private final static double MAX_SPEED = 0.7;


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
    abstract void update(ShadowDimension gameObject);
    abstract void render();
}
