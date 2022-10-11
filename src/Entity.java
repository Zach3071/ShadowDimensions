import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

public abstract class Entity {
    abstract Point getPosition();
    abstract Image getCurrentImage();
    abstract void moveBack();
    abstract Rectangle getBoundingBox();
    abstract void loseHealth(double healthLost);
    abstract boolean isDead();
}
