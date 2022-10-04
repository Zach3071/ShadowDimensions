import bagel.util.Point;
import bagel.util.Rectangle;

public class Boundary {
    private Point topLeft;
    private Point bottomRight;



    Boundary(Point topLeft, Point bottomRight){
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;

    }
    // Another constructor if you don't want to use the point class
    Boundary(double topLeftX, double topLeftY, double bottomRightX, double bottomRightY){
        Point topLeft = new Point(topLeftX, topLeftY);
        Point bottomRight = new Point(bottomRightX, bottomRightX);
    }
    Boundary() {

    }

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
