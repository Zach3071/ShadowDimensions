import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

public class Sinkhole {
    Point position;
    private Image sinkhole;

    private boolean isDeleted = false;


    Sinkhole(Point position) {
        this.position = position;
        sinkhole = new Image("res/sinkhole.png");
    }

    public void render() { sinkhole.drawFromTopLeft(position.x, position.y); }

    public Rectangle getBoundingBox(){
        Rectangle barrier = sinkhole.getBoundingBox();
        barrier.moveTo(position);
        return barrier;
    }

    public Point getSinkholePosition(){
        return position;
    }

    public void setDeleted() { isDeleted = true; }
    public boolean isDeleted() { return isDeleted; }
}
