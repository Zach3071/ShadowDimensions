import bagel.Image;
import bagel.util.Rectangle;
import bagel.util.Point;

import java.time.Year;

public class Wall {
    public final double xPosition;
    private final double yPosition;
    private Image wall;

    Wall(double xPosition, double yPosition){
        wall = new Image("res/wall.png");
        this.xPosition = xPosition;
        this.yPosition = yPosition;
    }

    public void drawWall(){
        wall.drawFromTopLeft(xPosition, yPosition);
    }

    public Rectangle getBoundingBox(){
        Point barrier = new Point(xPosition, yPosition);
        Rectangle barr = wall.getBoundingBox();
        barr.moveTo(barrier);
        return barr;
    }

}
