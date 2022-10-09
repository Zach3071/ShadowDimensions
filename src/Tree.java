import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

public class Tree{
    private Point position;

    private Image tree = new Image("res/tree.png");

    Tree(Point position){
        this.position = position;
    }

    public void render() {
        tree.drawFromTopLeft(this.position.x, this.position.y);
    }

    public Rectangle getBoundingBox() {
        return new Rectangle(position, tree.getWidth(), tree.getHeight());
    }
}
