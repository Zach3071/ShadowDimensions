import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

public class Sinkhole {
    Point position;
    private Image sinkhole;


    Sinkhole(Point position) {
        this.position = position;
        sinkhole = new Image("res/sinkhole.png");
    }

    public void drawSinkhole() {
        sinkhole.drawFromTopLeft(position.x, position.y);
    }

    public Rectangle getBoundingBox(){
        Rectangle barrier = sinkhole.getBoundingBox();
        barrier.moveTo(position);
        return barrier;
    }

    public Point getSinkholePosition(){
        return position;
    }

}
