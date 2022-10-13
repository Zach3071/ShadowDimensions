import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

/** This class contains the properties and attributes for sinkholes */
public class Sinkhole {
    private Point position;
    private Image sinkhole;

    private boolean isDeleted = false;


    Sinkhole(Point position) {
        this.position = position;
        sinkhole = new Image("res/sinkhole.png");
    }

    /** This renders the sinkhole's image */
    public void render() { sinkhole.drawFromTopLeft(position.x, position.y); }

    public Rectangle getBoundingBox(){
        Rectangle barrier = sinkhole.getBoundingBox();
        barrier.moveTo(position);
        return barrier;
    }

    public void setDeleted() { isDeleted = true; }
    public boolean isDeleted() { return isDeleted; }
}
