import bagel.Image;
import bagel.util.Point;

public abstract class Entity {
    abstract Point getPosition();
    abstract Image getCurrentImage();
    abstract void moveBack();
}
