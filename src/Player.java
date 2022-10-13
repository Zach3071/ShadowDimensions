import bagel.*;
import bagel.util.Point;
import bagel.util.Rectangle;

// partial project1 solution has been used

/**
 * This class contains properties of the main character in the game which
 * attempts to pass each level
 */
public class Player extends Entity implements Moveable, Invincible {
    private final static String FAE_LEFT = "res/fae/faeLeft.png";
    private final static String FAE_RIGHT = "res/fae/faeRight.png";
    private final static String ATTACK_LEFT = "res/fae/faeAttackLeft.png";
    private final static String ATTACK_RIGHT = "res/fae/faeAttackRight.png";
    private static final double INVINCIBLE_LENGTH = 3000;
    public final static double MOVE_SIZE = 2;
    private boolean facingRight;
    private Point position;
    private Point prevPosition;
    private Image currentImage;

    private double currentHealth;
    private final double MAX_HEALTH = 100;
    private final double MIN_HEALTH = 0;
    private final int DAMAGE = 20;
    private boolean isAttacking;
    private int currentFrameCount = 0;
    private int initialFrameCount = 0;
    private final static double ATTACK_DELAY = 2000;
    private final static double ATTACK_LENGTH = 1000;
    private final static double HEALTH_BAR_X = 20;
    private final static double HEALTH_BAR_Y = 25;
    private final static int HEALTH_BAR_FONT_SIZE = 30;
    private boolean isInvincible = false;
    private boolean checkedInitialFrame;
    HealthBar healthBar = new HealthBar(currentHealth, MAX_HEALTH);

    Player(){
        this.currentHealth = MAX_HEALTH;
        this.currentImage = new Image(FAE_RIGHT);
        this.facingRight = true;
    }

    public double getPlayerX() {
        return position.x;
    }
    public double getPlayerY() {
        return position.y;
    }
    public Point getPosition() {
        return position;
    }

    public Image getCurrentImage() {
        return currentImage;
    }

    public void setPosition(double playerX, double playerY) {
        this.position = new Point(playerX, playerY);
    }

    @Override
    boolean isDead() { return currentHealth <= MIN_HEALTH;}

    /** Returns player's current health
     * @return This is the player's current health
     */
    public double getCurrentHealth(){
        if (currentHealth <= MIN_HEALTH) {
            currentHealth = MIN_HEALTH;
        }
        return currentHealth;
    }

    /** Player loses health
     * @param healthLost This is the amount of health lost
     */
    public void loseHealth(double healthLost){ currentHealth -= healthLost; }
    public double getMaxHealth(){ return MAX_HEALTH; }
    public void setHealth(double health) {
        currentHealth = health;
    }

    /** This update's the player's movement and special properties,
     * such as attacking, invincibility and collisions etc.
     * @param input This is the input taken from the user
     * @param gameObject This is a refernce to the object itself in the level manager class
     */
    public void update(Input input, LevelManager gameObject){
        if (input.isDown(Keys.UP)){
            setPrevPosition();
            move(0, - MOVE_SIZE);
        } else if (input.isDown(Keys.DOWN)){
            setPrevPosition();
            move(0, MOVE_SIZE);
        } else if (input.isDown(Keys.LEFT)){
            setPrevPosition();
            move(-MOVE_SIZE,0);

            if (facingRight) {
                this.currentImage = new Image(FAE_LEFT);
                facingRight = !facingRight;
            }
        } else if (input.isDown(Keys.RIGHT)){
            setPrevPosition();
            move(MOVE_SIZE,0);
            if (!facingRight) {
                this.currentImage = new Image(FAE_RIGHT);
                facingRight = !facingRight;
            }
        }
        initiateAttack(input);

        if (isInvincible) {
            renderInvincibility(gameObject);
        }

        this.currentImage.drawFromTopLeft(position.x, position.y);
        gameObject.checkCollisions(this);
        gameObject.checkOutOfBounds(this);

        healthBar.drawHealthBar(HEALTH_BAR_X, HEALTH_BAR_Y, HEALTH_BAR_FONT_SIZE, currentHealth, MAX_HEALTH);
        currentFrameCount++;
    }

    private void initiateAttack(Input input) {
        if (!isAttacking) {
            // adds a 2-second cool down timer, which prevents user from attacking *****************************
            if (((ShadowDimension.REFRESH_RATE / ShadowDimension.MILLI_SECONDS) * (ATTACK_DELAY+1000))
                    <= currentFrameCount - initialFrameCount && input.wasPressed(Keys.A)) {
                    initialFrameCount = currentFrameCount;
                    isAttacking = true;
            }
        } else {
            if (facingRight) {
                this.currentImage = new Image(ATTACK_RIGHT);
            } else {
                this.currentImage = new Image(ATTACK_LEFT);
            }
        }
        // this returns attacking state to false after 1 second of being active
        if (((ShadowDimension.REFRESH_RATE/ ShadowDimension.MILLI_SECONDS) * ATTACK_LENGTH)
                <= currentFrameCount - initialFrameCount) {
            isAttacking = false;
            if (facingRight) {
                this.currentImage = new Image(FAE_RIGHT);
            }
            else {
                this.currentImage = new Image(FAE_LEFT);
            }
        }
    }

    /** This method attempts to a given enemy
     * @param enemy This is the enemy attempting to be attacked
     * @return Returns true if attack was successful
     */
    public boolean attack(DemonEnemy enemy) {
        Rectangle entityBox = enemy.getBoundingBox();
        if (entityBox.intersects(getBoundingBox())) {
            enemy.loseHealth(DAMAGE);
            System.out.println("Fae inflicts " + DAMAGE + " damage points on " + enemy.getClass().getSimpleName() +
                    ". " + enemy.getClass().getSimpleName() + "'s current health: " + (int)enemy.getCurrentHealth() +
                    "/" + (int)enemy.getMaxHealth());
            return true;
        }
        return false;
    }

    public void setPrevPosition(){ this.prevPosition = new Point(position.x, position.y); }
    /** Moves the player back to its previous position, usually used for collisions */
    public void moveBack(){ this.position = prevPosition; }

    /** Moves the player to its new coordinates
     * @param xMove This is the new x coordinate
     * @param yMove This is the new y coordinate
     */
    public void move(double xMove, double yMove){
        double newX = position.x + xMove;
        double newY = position.y + yMove;
        this.position = new Point(newX, newY);
    }
    @Override
    public Rectangle getBoundingBox() {
        Rectangle entityBox = new Rectangle(position, getCurrentImage().getWidth(),
                getCurrentImage().getHeight());
        return entityBox;
    }
    public boolean isAttacking(){ return isAttacking; }

    public boolean isInvincible() { return isInvincible; }
    /** Sets the players invincibility to true */
    @Override
    public void triggerInvincibility() { isInvincible = true; }

    /** Starts the invincibility timer for the player if the player
     * has been triggered as invincible
     * @param gameObject
     */
    public void renderInvincibility(LevelManager gameObject) {
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

}
