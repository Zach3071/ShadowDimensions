import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

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
    private boolean isInvincible;
    private boolean facingLeft;
    private int currentFrameCount;
    private int initialFrameCount;
    private boolean checkedInitialFrame = false;
    private final double INVINCIBLE_LENGTH = INVINCIBLE_TIME;
    private double MAX_HEALTH = DEMON_HEALTH;
    private double currentHealth = MAX_HEALTH;
    HealthBar healthBar = new HealthBar(currentHealth, MAX_HEALTH);

    Demon(Point position) {
        this.position = position;
        this.isAggressive = randomBoolean();
        // faces and makes demon left and stationary by default
        this.currentImage = new Image(DEMON_LEFT);
        this.direction = "Stationary";
        this.facingLeft = true;

        if (isAggressive) {
            direction = String.valueOf(Direction.setRandomDirection());
            SPEED = setRandomSpeed();

            if (direction.equals("RIGHT")) {
                this.facingLeft = false;
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
        if (isInvincible) {
            initiateInvincibility(gameObject);
        }
        if (!isInvincible) {
            if (facingLeft) {
                currentImage = new Image(DEMON_LEFT);
            } else {
                currentImage = new Image(DEMON_RIGHT);
            }
        }
        healthBar.drawHealthBar(position.x, position.y-6, HEALTHBAR_FONT, currentHealth, MAX_HEALTH);
        gameObject.checkOutOfBounds(this);
        gameObject.checkCollisions(this);
        render();
        currentFrameCount++;
    }

    // turns object invincible if attacked
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

    // used for determining if demon is passive or aggressive
    private boolean randomBoolean() {
        // 0.5 used for probability of random boolean
        return Math.random() < 0.5;
    }

    @Override
    public void move(double xMove, double yMove) {
        double newX = position.x + xMove;
        double newY = position.y + yMove;
        this.position = new Point(newX, newY);
    }

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
                    currentImage = new Image(DEMON_LEFT);
                }
                break;
            case "LEFT":
                direction = "RIGHT";
                facingLeft = false;
                if (!isInvincible) {
                    currentImage = new Image(DEMON_RIGHT);
                }
                break;
        }
    }

    @Override
    public Point getPosition() { return position; }

    @Override
    public Image getCurrentImage() { return currentImage; }

    @Override
    public Rectangle getBoundingBox() {
        Rectangle entityBox = new Rectangle(position, getCurrentImage().getWidth(),
                getCurrentImage().getHeight());
        return entityBox;
    }




    public void setInvincible() { isInvincible = true; }
    public boolean isInvincible() { return isInvincible; }
    public void loseHealth(double healthLost){ currentHealth -= healthLost; }
    public boolean isDead() { return currentHealth <= MIN_HEALTH; }
}
