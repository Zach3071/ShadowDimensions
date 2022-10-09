import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

public abstract class WorldObjects {
    protected Point position;
    protected Image image;

    public void render() {
        image.drawFromTopLeft(this.position.x, this.position.y);
    }

    public Rectangle getBoundingBox() {
        return new Rectangle(position, image.getWidth(), image.getHeight());
    }
}
