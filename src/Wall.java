import bagel.Image;
import bagel.util.Rectangle;
import bagel.util.Point;

/** This class contains the properties and attributes for walls */
public class Wall {
    private final double xPosition;
    private final double yPosition;
    private Image wall;

    Wall(double xPosition, double yPosition){
        wall = new Image("res/wall.png");
        this.xPosition = xPosition;
        this.yPosition = yPosition;
    }
    /** Draws the image of the wall */
    public void render(){ wall.drawFromTopLeft(xPosition, yPosition); }

    public Rectangle getBoundingBox(){
        Point barrier = new Point(xPosition, yPosition);
        Rectangle barr = wall.getBoundingBox();
        barr.moveTo(barrier);
        return barr;
    }

}
