import java.util.Random;

/** This abstracts class contains the important attributes and methods
 * shared between navec and demons
 */
public abstract class DemonEnemy extends Entity implements Moveable, Invincible{
    protected final double INVINCIBLE_TIME = 3000;
    protected final int DEMON_DAMAGE = 10;
    protected final int DEMON_HEALTH = 40;
    protected final int MIN_HEALTH = 0;
    private final static double MIN_SPEED = 0.2;
    private final static double MAX_SPEED = 0.7;
    protected final int HEALTHBAR_FONT = 15;

    /** This is used by demonEnemies to attack
     * a player
     * @param player This is the player being used
     * @return Returns true if attack was successful
     */
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

    /** This supplies a random speed within the MAX and MIN speed
     * intervals for demonEnemies
     * @return This is a random speed number generated within an interval
     */
    public double setRandomSpeed() {
        Random r = new Random();
        double randomSpeed = MIN_SPEED + (MAX_SPEED - MIN_SPEED) * r.nextDouble();
        return randomSpeed;
    }

    /** Used to update a demonEnemy's movement, image and properties */
    abstract void update(LevelManager gameObject);
    /** Used to update a demons speed if the timescale has changed */
    abstract void updateTimescaleSpeed();
    /** Render's the demons image */
    abstract void render();
}
