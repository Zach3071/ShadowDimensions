import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

/** This abstract class is used by objects that
 * can move and have a mind of their own
 */
public abstract class Entity {
    abstract Point getPosition();
    abstract Image getCurrentImage();
    /** Used to rebound an entity from collisions */
    abstract void moveBack();
    abstract Rectangle getBoundingBox();
    abstract void loseHealth(double healthLost);
    abstract double getCurrentHealth();
    abstract double getMaxHealth();
    abstract boolean isDead();
}
