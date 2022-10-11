import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

public class Navec extends DemonEnemy{
    private final static String NAVEC_LEFT = "res/navec/navecLeft.png";
    private final static String NAVEC_RIGHT = "res/navec/navecRight.png";
    private final static String INVINCIBLE_LEFT = "res/navec/navecInvincibleLeft.png";
    private final static String INVINCIBLE_RIGHT = "res/navec/navecInvincibleRight.png";
    private final static String NAVEC_FIRE = "res/navec/navecFire.png";
    private String direction;
    private double SPEED;
    private Image currentImage;
    private Point position;
    private boolean isInvincible;
    private boolean facingLeft;
    private int currentFrameCount;
    private int initialFrameCount;
    private boolean checkedInitialFrame = false;
    private final double INVINCIBLE_LENGTH = INVINCIBLE_TIME;
    private double MAX_HEALTH = DEMON_HEALTH*2;
    private double currentHealth = MAX_HEALTH;
    HealthBar healthBar = new HealthBar(currentHealth, MAX_HEALTH);


    Navec(Point position) {
        this.position = position;
        this.direction = String.valueOf(Direction.setRandomDirection());
        this.SPEED = setRandomSpeed();
        this.currentImage = new Image(NAVEC_LEFT);
        this.facingLeft = true;
        if (direction.equals("RIGHT")) {
            this.currentImage = new Image(NAVEC_RIGHT);
            this.facingLeft = false;
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
        if (isInvincible) {
            initiateInvincibility(gameObject);
        }
        if (!isInvincible) {
            if (facingLeft) {
                currentImage = new Image(NAVEC_LEFT);
            } else {
                currentImage = new Image(NAVEC_RIGHT);
            }
        }
        healthBar.drawHealthBar(position.x, position.y-6, HEALTHBAR_FONT, currentHealth, MAX_HEALTH);
        gameObject.checkOutOfBounds(this);
        gameObject.checkCollisions(this);
        render();
        currentFrameCount++;
    }

    public void initiateInvincibility(ShadowDimension gameObject) {
        if (facingLeft) {
            currentImage = new Image(INVINCIBLE_LEFT);
        } else {
            currentImage = new Image(INVINCIBLE_RIGHT);
        }
        // starts invincibility timer
        if (!checkedInitialFrame) {
            initialFrameCount = currentFrameCount;
            checkedInitialFrame = true;
        }
        // checks if invincibility timer has been reached
        if (((ShadowDimension.REFRESH_RATE/ ShadowDimension.MILLI_SECONDS) * INVINCIBLE_LENGTH)
                <= currentFrameCount - initialFrameCount) {
            isInvincible = false;
            checkedInitialFrame = false;
        }
    }

    @Override
    public void render() {
        this.currentImage.drawFromTopLeft(position.x, position.y);
    }


    public double getSpeed() { return SPEED; }

    @Override
    public void move(double xMove, double yMove) {
        double newX = position.x + xMove;
        double newY = position.y + yMove;
        this.position = new Point(newX, newY);
    }

    @Override
    Point getPosition() { return position; }

    @Override
    Image getCurrentImage() { return currentImage; }

    @Override
    public void moveBack() {
        switch (direction) {
            case "UP":
                direction = "DOWN";
                break;
            case "DOWN":
                direction = "UP";
                break;
            case "RIGHT":
                direction = "LEFT";
                facingLeft = true;
                if (!isInvincible) {
                    currentImage = new Image(NAVEC_LEFT);
                }
                break;
            case "LEFT":
                direction = "RIGHT";
                facingLeft = false;
                if (!isInvincible) {
                    currentImage = new Image(NAVEC_RIGHT);
                }
                break;
        }
    }
    @Override
    public Rectangle getBoundingBox() {
        Rectangle entityBox = new Rectangle(position, getCurrentImage().getWidth(),
                getCurrentImage().getHeight());
        return entityBox;
    }

    @Override
    void loseHealth(double healthLost) { currentHealth -= healthLost; }

    @Override
    boolean isDead() { return currentHealth <= MIN_HEALTH; }


    public void setInvincible() { isInvincible = true; }
    public boolean isInvincible() { return isInvincible; }


}
