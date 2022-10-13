import bagel.util.Point;

/** This class contains the world border
 * which contains objects
 */
public class Boundary {
    private Point topLeft;
    private Point bottomRight;


    Boundary() {}


    public void setTopLeft(Point topLeft){
        this.topLeft = topLeft;
    }


    public void setBottomRight(Point bottomRight){
        this.bottomRight = bottomRight;
    }


    public double getLeftX(){
        return topLeft.x;
    }


    public double getRightX(){
        return bottomRight.x;
    }

    public double getTopY(){
        return topLeft.y;
    }

    public double getBottomY(){
        return bottomRight.y;
    }
}
