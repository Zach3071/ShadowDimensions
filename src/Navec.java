import bagel.Image;
import bagel.util.Point;

public class Navec extends DemonEnemy{
    private final static String NAVEC_LEFT = "res/navec/navecLeft.png";
    private final static String NAVEC_RIGHT = "res/navec/navecRight.png";
    private final static String INVINCIBLE_LEFT = "res/navec/navecInvincibleLeft.png";
    private final static String INVINCIBLE_RIGHT = "res/navec/navecInvincibleRight.png";
    private final static String NAVEC_FIRE = "res/navec/navecFire.png";
    private String direction;
    private double speed;
    private Image currentImage;
    private Point position;


    Navec(Point position) {
        this.position = position;

    }

    @Override
    void update(ShadowDimension gameObject) {

    }

    @Override
    public void render() {
        this.currentImage.drawFromTopLeft(position.x, position.y);
    }

    public String getDirection() { return direction; }
    public double getSpeed() { return speed; }

    @Override
    public void move(double xMove, double yMove) {

    }

    @Override
    Point getPosition() { return position; }

    @Override
    Image getCurrentImage() { return currentImage; }

    @Override
    public void moveBack() {

    }

    @Override
    public void setPrevPosition() {

    }
}
