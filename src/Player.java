import bagel.*;
import bagel.util.Point;
import bagel.util.Rectangle;

// partial project1 solution has been used
public class Player extends Entity implements Moveable, Invincible {
    private final static String FAE_LEFT = "res/fae/faeLeft.png";
    private final static String FAE_RIGHT = "res/fae/faeRight.png";
    private final static String ATTACK_LEFT = "res/fae/faeAttackLeft.png";
    private final static String ATTACK_RIGHT = "res/fae/faeAttackRight.png";
    private static final double INVINCIBLE_LENGTH = 3000;
    private double playerX;
    private double playerY;
    private final Image faeLeft;
    private final Image faeRight;
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
        faeLeft = new Image("res/fae/faeLeft.png");
        faeRight = new Image("res/fae/faeRight.png");
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
        this.playerX = playerX;
        this.playerY = playerY;
    }

    @Override
    boolean isDead() { return currentHealth <= MIN_HEALTH;}

    public double getCurrentHealth(){
        if (currentHealth <= MIN_HEALTH) {
            currentHealth = MIN_HEALTH;
        }
        return currentHealth;
    }

    public void loseHealth(double healthLost){ currentHealth -= healthLost; }
    public double getMaxHealth(){ return MAX_HEALTH; }
    public void setHealth(double health) {
        currentHealth = health;
    }

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

    public void moveBack(){ this.position = prevPosition; }

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

    @Override
    public void triggerInvincibility() { isInvincible = true; }

    //@Override
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
