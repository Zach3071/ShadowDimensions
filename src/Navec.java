import bagel.DrawOptions;
import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;
/** This class is a type of demon that is the final boss in level 1 */
public class Navec extends DemonEnemy{
    private final static String NAVEC_LEFT = "res/navec/navecLeft.png";
    private final static String NAVEC_RIGHT = "res/navec/navecRight.png";
    private final static String INVINCIBLE_LEFT = "res/navec/navecInvincibleLeft.png";
    private final static String INVINCIBLE_RIGHT = "res/navec/navecInvincibleRight.png";
    private final static String NAVEC_FIRE = "res/navec/navecFire.png";
    private String direction;
    private double SET_SPEED;
    private double speed;
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
    private Image fireImage;
    private final double PI = 3.142;
    private final double ATTACK_RANGE = 200;
    private DrawOptions fireRotation = new DrawOptions();
    private final int NAVEC_DAMAGE = DEMON_DAMAGE*2;

    Navec(Point position) {
        this.position = position;
        // uses demonEntity function to give a random direction
        this.direction = String.valueOf(Direction.setRandomDirection());

        // uses demonEntity function to give a random speed within a set interval
        this.SET_SPEED = setRandomSpeed();

        this.speed = this.SET_SPEED;

        this.currentImage = new Image(NAVEC_LEFT);
        this.facingLeft = true;
        this.fireImage = new Image(NAVEC_FIRE);

        if (direction.equals("RIGHT")) {
            this.currentImage = new Image(NAVEC_RIGHT);
            this.facingLeft = false;
        }
    }


    /** This updates and renders Navec's movement and properties
     * @param gameObject Used to refer to the object itself in level manager class
     */
    @Override
    public void update(LevelManager gameObject) {
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
                currentImage = new Image(NAVEC_LEFT);
            } else {
                currentImage = new Image(NAVEC_RIGHT);
            }
        }

        // Checks if timescale speed has been updated
        updateTimescaleSpeed();

        healthBar.drawHealthBar(position.x, position.y-6, HEALTHBAR_FONT, currentHealth, MAX_HEALTH);

        gameObject.checkOutOfBounds(this);
        gameObject.checkCollisions(this);
        render();
        currentFrameCount++;
    }
    /** Checks if timescale speed has been changed and updates Navec's speed */
    public void updateTimescaleSpeed() {
        if (Timescale.getTimescale() != 0 && !Timescale.hasTimescaleUpdated()) {
            if (Timescale.getTimescale() > 0) {
                speed = SET_SPEED * Math.pow(1.5, Timescale.getTimescale());
            } else {
                speed = SET_SPEED * Math.pow(.5, -Timescale.getTimescale());
            }
        }
        else if (Timescale.getTimescale() == 0 && !Timescale.hasTimescaleUpdated()) {
            speed = SET_SPEED;
        }
    }
    /** This renders the demon
     *  as invincible for 3000 milliseconds and
     *  renders it with its invincible image
     * @param gameObject
     */
    public void renderInvincibility(LevelManager gameObject) {
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
    /** This renders the current image
     * for Navec
     */
    @Override
    public void render() {
        this.currentImage.drawFromTopLeft(position.x, position.y);
    }

    /** This updates Navec's position
     * to a new position, this is used to
     * display the demon moving in the update
     * function
     * @param xMove This is the new x coordinate
     * @param yMove This is the new y coordinate
     */
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

    /** This rebounds Navec into the opposite
     * direction. Usually when it has collided with
     * an object
     */
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
    /** Navec loses a certain amount of
     * health
     * @param healthLost This is the amount of health being lost
     */
    @Override
    public void loseHealth(double healthLost) { currentHealth -= healthLost; }

    @Override
    public double getCurrentHealth() { return currentHealth; }

    @Override
    public double getMaxHealth() { return (int)MAX_HEALTH; }

    @Override
    public boolean isDead() { return currentHealth <= MIN_HEALTH; }

    /** Demon's invincible attribute is set
     * to true
     */
    public void triggerInvincibility() { isInvincible = true; }
    public boolean isInvincible() { return isInvincible; }

    /** Navec renders a fire depending on the location of the player
     *
     * @param playerCentre This is the centre of the player's coordinates
     * @param demonCentre This is the centre coordinates of Navec
     * @param demonBox This is the Navec's rectangle
     * @param player This is the player being used
     * @return This returns true if the player has intersected with a fire's rectangle box
     */
    public boolean fireAttack(Point playerCentre, Point demonCentre, Rectangle demonBox, Player player) {
        // fire is rendered at top-left of demon
        if (playerCentre.x <= demonCentre.x && playerCentre.y <= demonCentre.y) {
            fireImage.drawFromTopLeft(demonBox.topLeft().x - fireImage.getWidth(), demonBox.topLeft().y - fireImage.getHeight());

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
            fireImage.drawFromTopLeft(demonBox.topRight().x, demonBox.topRight().y - fireImage.getHeight(), fireRotation);

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
    /** This renders the fire if the player is
     * within Navec's attacking range
     * @param player This is the player being used
     * @return This returns true if the player has been successfully attacked
     */
    public boolean attack(Player player) {
        Point playerCentre = player.getBoundingBox().centre();
        Rectangle demonBox = getBoundingBox();
        Point demonCentre = getBoundingBox().centre();

        if (getBoundingBox().centre().distanceTo(playerCentre) <= ATTACK_RANGE) {
            if (fireAttack(playerCentre, demonCentre, demonBox, player) && !player.isInvincible()) {

                player.loseHealth(NAVEC_DAMAGE);

                System.out.println(getClass().getSimpleName() + " inflicts " + NAVEC_DAMAGE + " damage points on Fae"  +
                        ". Fae's current health: " + (int)player.getCurrentHealth() +
                        "/" + (int)player.getMaxHealth());
                return true;
            }
        }
        return false;
    }

}
