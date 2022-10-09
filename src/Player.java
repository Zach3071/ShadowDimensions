import bagel.*;
import bagel.util.Point;
import bagel.util.Rectangle;
// partial project1 solution has been used
public class Player extends Entity implements Moveable {
    private final static String FAE_LEFT = "res/fae/faeLeft.png";
    private final static String FAE_RIGHT = "res/fae/faeRight.png";
    private double playerX;
    private double playerY;
    private final Image faeLeft;
    private final Image faeRight;
    public final static double MOVE_SIZE = 2;
    private boolean facingRight;
    private Point position;
    private Point prevPosition;
    private Image currentImage;

    private double currentHealth = 100;
    private final double MAX_HEALTH = 100;
    private final double MIN_HEALTH = 0;

    Player(double startingPosX, double startingPosY) {
        faeLeft = new Image("res/fae/faeLeft.png");
        faeRight = new Image("res/fae/faeRight.png");
        this.playerX = startingPosX;
        this.playerY = startingPosY;
        this.position = new Point(startingPosX, startingPosY);
    }

    Player(){
        faeLeft = new Image("res/fae/faeLeft.png");
        faeRight = new Image("res/fae/faeRight.png");
        this.currentHealth = MAX_HEALTH;
        this.currentImage = new Image(FAE_RIGHT);
        this.facingRight = true;
        //COLOUR.setBlendColour(GREEN);
    }

    public void setPlayerX(double playerX ) {
        this.playerX = playerX;
    }

    public void setPlayerY(double playerY){
        this.playerY = playerY;
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




    public Rectangle getBoundingBox(){


        Rectangle barr = faeLeft.getBoundingBox();
        barr.moveTo(position);
        return barr;
    }



    public void loseHealth(double healthLost){
        currentHealth -= healthLost;
    }

    public double getCurrentHealth(){
        if (currentHealth <= MIN_HEALTH) {
            currentHealth = MIN_HEALTH;
        }
        return currentHealth;
    }

    public double getMaxHealth(){
        return MAX_HEALTH;
    }

    public double getMinHealth() {
        return MIN_HEALTH;
    }





    public void update(Input input, ShadowDimension gameObject){
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
        this.currentImage.drawFromTopLeft(position.x, position.y);
        gameObject.checkCollisions(this);
        //renderHealthPoints();
        gameObject.checkOutOfBounds(this);
    }

    public void setPrevPosition(){
        this.prevPosition = new Point(position.x, position.y);
    }

    public void moveBack(){
        this.position = prevPosition;
    }

    public void move(double xMove, double yMove){
        double newX = position.x + xMove;
        double newY = position.y + yMove;
        this.position = new Point(newX, newY);
    }

}
