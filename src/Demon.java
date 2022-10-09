import bagel.*;
import bagel.Image;
import bagel.util.Point;

import bagel.util.Rectangle;

import javax.swing.text.Position;

public class Demon extends DemonEnemy {
    private final static String DEMON_LEFT = "res/demon/demonLeft.png";
    private final static String DEMON_RIGHT = "res/demon/demonRight.png";
    private final static String INVINCIBLE_LEFT = "res/demon/demonInvincibleLeft.png";
    private final static String INVINCIBLE_RIGHT = "res/demon/demonInvincibleRight.png";
    private final static String DEMON_FIRE = "res/demon/demonFire.png";
    private String direction;
    private final boolean isAggressive;
    private double SPEED;
    private Image currentImage;
    private Point position;

    Demon(Point position) {
        this.position = position;
        this.isAggressive = randomBoolean();
        // sets demon to left by default
        this.currentImage = new Image(DEMON_LEFT);

        this.direction = "Stationary";
        if (isAggressive) {
            direction = String.valueOf(Direction.setRandomDirection());
            SPEED = setRandomSpeed();

            if (direction.equals("RIGHT")) {
                this.currentImage = new Image(DEMON_RIGHT);
            }
        }
    }


    @Override
    void update(ShadowDimension gameObject) {
        if (direction.equals("UP")) {
            move(0, - SPEED);
        } else if (direction.equals("DOWN")) {
            move(0, SPEED);
        } else if (direction.equals("LEFT")) {
            move(- SPEED, 0);
        } else if (direction.equals("RIGHT")) {
            move(SPEED, 0);
        }

        gameObject.checkOutOfBounds(this);
        gameObject.checkCollisions(this);
        render();
    }
//    private boolean checkCollision() {
//        Rectangle demonBox = new Rectangle(position, currentImage,
//                player.getCurrentImage().getHeight());
//    }
    @Override
    public void render() {
        this.currentImage.drawFromTopLeft(position.x, position.y);
    }

    public String getDirection() { return direction; }
    public double getSpeed() { return SPEED; }
    // used for determining if demon is passive or aggressive
    private boolean randomBoolean() {
        // 0.5 used for probability of random boolean
        return Math.random() < 0.5;
    }
    public boolean isAggressive() { return isAggressive; }

    @Override
    public void move(double xMove, double yMove) {
        double newX = position.x + xMove;
        double newY = position.y + yMove;
        this.position = new Point(newX, newY);
    }

    @Override
    public void moveBack() {
        if (direction.equals("UP")) {
            direction = "DOWN";
        }
        else if (direction.equals("DOWN")) {
            direction = "UP";
        }
        else if (direction.equals("RIGHT")) {
            direction = "LEFT";
        }
        else if (direction.equals("LEFT")) {
            direction = "RIGHT";
        }
    }

    @Override
    public void setPrevPosition() {

    }

    @Override
    Point getPosition() { return position; }

    @Override
    Image getCurrentImage() { return currentImage; }

}
