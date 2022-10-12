import bagel.DrawOptions;
import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

import java.sql.Time;

public class Demon extends DemonEnemy {
    private final static String DEMON_LEFT = "res/demon/demonLeft.png";
    private final static String DEMON_RIGHT = "res/demon/demonRight.png";
    private final static String INVINCIBLE_LEFT = "res/demon/demonInvincibleLeft.png";
    private final static String INVINCIBLE_RIGHT = "res/demon/demonInvincibleRight.png";
    private final static String DEMON_FIRE = "res/demon/demonFire.png";
    private String direction;
    private final boolean isAggressive;
    private double SET_SPEED = 0;
    private double speed;
    private Image currentImage;
    private Image fireImage;
    private final double PI = 3.142;
    private DrawOptions fireRotation = new DrawOptions();
    private Point position;
    private boolean isInvincible;
    private boolean facingLeft;
    private int currentFrameCount;
    private int initialFrameCount;
    private boolean checkedInitialFrame = false;
    private final double INVINCIBLE_LENGTH = INVINCIBLE_TIME;
    private double MAX_HEALTH = DEMON_HEALTH;
    private double currentHealth = MAX_HEALTH;
    private final double ATTACK_RANGE = 150;
    HealthBar healthBar = new HealthBar(currentHealth, MAX_HEALTH);

    Demon(Point position) {
        this.position = position;
        this.isAggressive = randomBoolean();
        // faces and makes demon left and stationary by default
        this.currentImage = new Image(DEMON_LEFT);
        this.direction = "Stationary";
        this.facingLeft = true;
        this.fireImage = new Image(DEMON_FIRE);
        if (isAggressive) {
            direction = String.valueOf(Direction.setRandomDirection());
            this.SET_SPEED = setRandomSpeed();
            this.speed = this.SET_SPEED;
            if (direction.equals("RIGHT")) {
                this.facingLeft = false;
                this.currentImage = new Image(DEMON_RIGHT);
            }
        }
    }

    @Override
    void update(ShadowDimension gameObject) {
        if (direction.equals("UP")) {
            move(0, - speed);
        } else if (direction.equals("DOWN")) {
            move(0, speed);
        } else if (direction.equals("LEFT")) {
            move(- speed, 0);
        } else if (direction.equals("RIGHT")) {
            move(speed, 0);
        }
        if (isInvincible) {
            renderInvincibility(gameObject);
        }
        if (!isInvincible) {
            if (facingLeft) {
                currentImage = new Image(DEMON_LEFT);
            } else {
                currentImage = new Image(DEMON_RIGHT);
            }
        }

        updateTimescaleSpeed();

        healthBar.drawHealthBar(position.x, position.y-6, HEALTHBAR_FONT, currentHealth, MAX_HEALTH);
        gameObject.checkOutOfBounds(this);
        gameObject.checkCollisions(this);
        render();
        currentFrameCount++;
    }

     void updateTimescaleSpeed() {
        if (Timescale.getTimescale() != 0 && !Timescale.hasTimescaleUpdated()) {
            if (Timescale.getTimescale() > 0) {
                speed = SET_SPEED * Math.pow(1.5, Timescale.getTimescale());
            } else {
                speed = SET_SPEED * Math.pow(.5, -Timescale.getTimescale());
            }
            System.out.println(speed);
        }
        else if (Timescale.getTimescale() == 0 && !Timescale.hasTimescaleUpdated()) {
            speed = SET_SPEED;
        }
    }

    // turns object invincible if attacked
    public void renderInvincibility(ShadowDimension gameObject) {
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




    public void triggerInvincibility() { isInvincible = true; }
    public boolean isInvincible() { return isInvincible; }
    public void loseHealth(double healthLost){ currentHealth -= healthLost; }

    @Override
    double getCurrentHealth() { return currentHealth; }

    @Override
    double getMaxHealth() { return MAX_HEALTH; }

    public boolean isDead() { return currentHealth <= MIN_HEALTH; }

    public boolean fireAttack(Point playerCentre, Point demonCentre, Rectangle demonBox, Player player) {
        // fire is rendered at top-left of demon
        if (playerCentre.x <= demonCentre.x && playerCentre.y <= demonCentre.y) {
            fireImage.drawFromTopLeft(demonBox.topLeft().x - fireImage.getWidth(),
                    demonBox.topLeft().y - fireImage.getHeight());

            Rectangle fireBox = new Rectangle(demonBox.topLeft().x - fireImage.getWidth(),
                    demonBox.topLeft().y - fireImage.getHeight(), fireImage.getWidth(), fireImage.getHeight());

            // damages player if they touch the fire
            return player.getBoundingBox().intersects(fireBox);
        }
        // fire is rendered at bottom-left of demon
        else if (playerCentre.x <= demonCentre.x && playerCentre.y > demonCentre.y) {
            fireRotation.setRotation(-PI/2);
            fireImage.drawFromTopLeft(demonBox.bottomLeft().x - fireImage.getWidth(), demonBox.bottomLeft().y,
                    fireRotation);

            Rectangle fireBox = new Rectangle(demonBox.bottomLeft().x - fireImage.getWidth(),
                    demonBox.bottomLeft().y, fireImage.getWidth(), fireImage.getHeight());

            // damages player if they touch the fire
            return player.getBoundingBox().intersects(fireBox);
        }
        // fire is rendered at top-right  of demon
        else if (playerCentre.x > demonCentre.x && playerCentre.y <= demonCentre.y) {
            fireRotation.setRotation(PI/2);
            fireImage.drawFromTopLeft(demonBox.topRight().x, demonBox.topRight().y - fireImage.getHeight(),
                    fireRotation);

            Rectangle fireBox = new Rectangle(demonBox.topRight().x,
                    demonBox.topRight().y - fireImage.getHeight(), fireImage.getWidth(), fireImage.getHeight());

            // damages player if they touch the fire
            return player.getBoundingBox().intersects(fireBox);
        }
        // fire is rendered at bottom-right of demon
        else if (playerCentre.x > demonCentre.x && playerCentre.y > demonCentre.y) {
            fireRotation.setRotation(PI);
            fireImage.drawFromTopLeft(demonBox.bottomRight().x, demonBox.bottomRight().y, fireRotation);

            Rectangle fireBox = new Rectangle(demonBox.bottomRight().x,
                    demonBox.bottomRight().y, fireImage.getWidth(), fireImage.getHeight());

            // damages player if they touch the fire
            return player.getBoundingBox().intersects(fireBox);
        }
        return false;
    }




    public boolean attack(Player player) {
        Point playerCentre = player.getBoundingBox().centre();
        Rectangle demonBox = getBoundingBox();
        Point demonCentre = getBoundingBox().centre();

        if (getBoundingBox().centre().distanceTo(playerCentre) <= ATTACK_RANGE) {
            if (fireAttack(playerCentre, demonCentre, demonBox, player) && !player.isInvincible()) {

                player.loseHealth(DEMON_DAMAGE);

                System.out.println(getClass().getSimpleName() + " inflicts " + DEMON_DAMAGE + " damage points on Fae"  +
                        ". Fae's current health: " + (int)player.getCurrentHealth() +
                        "/" + (int)player.getMaxHealth());
                return true;
            }
        }
        return false;

    }

    public void increaseSpeed() {
        Timescale.getTimescale();
    }

//    public boolean fireAttack(Player player) {
//        Point playerCentre = player.getBoundingBox().centre();
//        Rectangle demonBox = getBoundingBox();
//        Point demonCentre = getBoundingBox().centre();
//
//        if (getBoundingBox().centre().distanceTo(playerCentre) <= ATTACK_RANGE) {
//            renderFire(playerCentre, demonCentre, demonBox, player);
//            return true;
//        }
//        return false;
//    }
}
