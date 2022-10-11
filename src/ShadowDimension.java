import bagel.*;
import bagel.util.Point;
import bagel.util.Rectangle;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

/**
 * Skeleton Code for SWEN20003 Project 1, Semester 2, 2022
 * Please enter your name below
 * @ ZACHARY KLIMAS, 1271087
 */

public class ShadowDimension extends AbstractGame {

    // window, title and message sizes
    public final static float REFRESH_RATE = 60;
    public final static float MILLI_SECONDS = 1000;
    private final static double LEVEL_COMPLETE_DELAY = 3000;
    int initialFrameCount = 0;
    private static int counter = 0;
    private final static int WINDOW_WIDTH = 1024;
    private final static int WINDOW_HEIGHT = 768;
    private final static int DEFAULT_FONT_SIZE = 75;
    private final static int MESSAGE_FONT_SIZE = 40;
    private final static int STARTING_TITLE_POSITION_X = 260;
    private final static int STARTING_TITLE_POSITION_Y = 250;
    private final static int MESSAGE_POSITION_X = STARTING_TITLE_POSITION_X + 90;
    private final static int MESSAGE_POSITION_Y = STARTING_TITLE_POSITION_Y + 190;
    private final static int LEVEL_ONE_MESSAGE_POS = 350;


    // font object used for big default titles, "CONGRATULATIONS!, GAME OVER! etc.
    Font titleMessage = new Font("res/frostbite.ttf", MESSAGE_FONT_SIZE);
    Font titleName = new Font("res/frostbite.ttf", DEFAULT_FONT_SIZE);
    Point startingTitlePosition = new Point(STARTING_TITLE_POSITION_X, STARTING_TITLE_POSITION_Y);
    Point messagePosition = new Point(MESSAGE_POSITION_X, MESSAGE_POSITION_Y);

    // main game messages displayed
    private final static String GAME_TITLE = "SHADOW DIMENSION";
    private final static String LEVEL_COMPLETE = "LEVEL COMPLETE!";
    private final static String GAME_WIN = "CONGRATULATIONS!";
    private final static String GAME_LOSS = "GAME OVER!";
    private final Image LEVEL_ZERO_BACKGROUND = new Image("res/background0.png");
    private final Image LEVEL_ONE_BACKGROUND = new Image("res/background1.png");

    // game screen boolean values
    private int gameLevel;
    private boolean hasStarted = false;
    private boolean isLevelZero = false;
    private boolean isLevelOne = false;
    private boolean levelCompletedScreen = false;
    private boolean losingScreen = false;

    // player movement and user input
    final static double WINNING_COORDINATES_X = 950;
    final static double WINNING_COORDINATES_Y = 670;



    private boolean playerInitialised = false;
    private boolean barrierHit;
    private static final double SINKHOLE_DAMAGE = 30;

    // game's outer wall boundary coordinates
    private double topLeftX;
    private double topLeftY;
    private double BottomRightX;
    private double BottomRightY;

    // object declarations
    Player Fae = new Player();
    List<Point> listOfSinkholesDeleted = new ArrayList<>();
    List<Wall> listOfWalls = new ArrayList<>();
    List<Sinkhole> listOfSinkholes = new ArrayList<>();
    List<Tree> listOfTrees = new ArrayList<>();
    List<DemonEnemy> listOfEnemies = new ArrayList<>();
    Boundary gameWall = new Boundary();

    private final static String LEVEL_ZERO_CSV = "res/level0.csv";
    private final static String LEVEL_ONE_CSV = "res/level1.csv";


    public ShadowDimension(){
        super(WINDOW_WIDTH, WINDOW_HEIGHT, GAME_TITLE);
        gameLevel = 0;
        // loads objects for level0, other levels are loaded in update function
        readCSV(LEVEL_ZERO_CSV);
    }

    /**
     * The entry point for the program.
     */
    public static void main(String[] args) {
        ShadowDimension game = new ShadowDimension();
        game.run();
    }

    /**
     * Method used to read file and create objects (You can change this
     * method as you wish).
     */
    private void readCSV(String levelFileName){

        String line;

        // corresponds to csv file's columns
        final int OBJ_COLUMN = 0;
        final int COLUMN_X = 1;
        final int COLUMN_Y = 2;

        try {
            BufferedReader br = new BufferedReader(new FileReader(levelFileName));
            listOfWalls.clear();
            listOfSinkholes.clear();
            playerInitialised = false;
            while((line = br.readLine()) != null) {
                String[] row = line.split(",");

                // Creates player object and initialises starting position
                if (row[OBJ_COLUMN].equals("Fae") && !playerInitialised){
                    Fae.setPosition(Integer.parseInt(row[COLUMN_X]), Integer.parseInt(row[COLUMN_Y]));
                    playerInitialised = true;
                }
                if (row[OBJ_COLUMN].equals("Wall")) {
                    //Implement objects here except player
                    Wall wall = new Wall(Integer.parseInt(row[COLUMN_X]), Integer.parseInt(row[COLUMN_Y]));
                    listOfWalls.add(wall);
                }
                if (row[OBJ_COLUMN].equals("Sinkhole")) {
                    Point sinkholePosition = new Point(Integer.parseInt(row[COLUMN_X]),
                            Integer.parseInt(row[COLUMN_Y]));
                    Sinkhole sinkhole = new Sinkhole(sinkholePosition);
                    listOfSinkholes.add(sinkhole);
                }
                if (row[OBJ_COLUMN].equals("Tree")) {
                    Point treePosition = new Point(Integer.parseInt(row[COLUMN_X]), Integer.parseInt(row[COLUMN_Y]));
                    Tree tree = new Tree(treePosition);
                    listOfTrees.add(tree);
                }
                if (row[OBJ_COLUMN].equals("Demon")) {
                    Point demonPosition = new Point(Integer.parseInt(row[COLUMN_X]), Integer.parseInt(row[COLUMN_Y]));
                    Demon demon = new Demon(demonPosition);
                    listOfEnemies.add(demon);

                }

                if (row[OBJ_COLUMN].equals("Navec")) {
                    Point navecPosition = new Point(Integer.parseInt(row[COLUMN_X]), Integer.parseInt(row[COLUMN_Y]));
                    Navec navec = new Navec(navecPosition);
                    listOfEnemies.add(navec);
                }

                // Initialises the game's wall boundary coordinates
                if (row[OBJ_COLUMN].equals("TopLeft")) {
                    topLeftX = Integer.parseInt(row[COLUMN_X]);
                    topLeftY = Integer.parseInt(row[COLUMN_Y]);
                }
                if (row[OBJ_COLUMN].equals("BottomRight")) {
                    BottomRightX = Integer.parseInt(row[COLUMN_X]);
                    BottomRightY = Integer.parseInt(row[COLUMN_Y]);
                }



                // clean this
                Point topLeft = new Point(topLeftX, topLeftY);
                Point bottomRight = new Point(BottomRightX, BottomRightY);
                gameWall.setTopLeft(topLeft);
                gameWall.setBottomRight(bottomRight);
                }
        }

        // checks for any errors with the csv file
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateObjects() {

        // updates and draws walls in game
        for (Wall wall : listOfWalls) {
            wall.render();
        }

        for (Sinkhole sinkhole : listOfSinkholes) {
            if (!sinkhole.isDeleted()) {
                sinkhole.render();
            }
        }

        for (Tree tree : listOfTrees) {
            tree.render();
        }

        for (DemonEnemy demon : listOfEnemies) {
            if (!demon.isDead()) {
                demon.update(this);
            }
        }

    }

    // draws the background image of the game
    private void displayLevelZero() {
        LEVEL_ZERO_BACKGROUND.draw(Window.getWidth() / 2.0, Window.getHeight() / 2.0);
    }

    private void displayLevelOne() {
        LEVEL_ONE_BACKGROUND.draw(Window.getWidth() / 2.0, Window.getHeight() / 2.0);
    }
    private void displayWinningScreen() {

        // Displays centred "CONGRATULATIONS!" text
        titleName.drawString(GAME_WIN, Window.getWidth()/2 - titleName.getWidth(GAME_WIN)/2, Window.getHeight()/2);
    }
    private void displayLevelComplete() {

        // Displays centred "LEVEL COMPLETE!" text
        titleName.drawString(LEVEL_COMPLETE, Window.getWidth()/2 - titleName.getWidth(LEVEL_COMPLETE)/2,
                Window.getHeight()/2);
    }

    private void displayLevelOneTitleScreen() {
        titleMessage.drawString("PRESS SPACE TO START\nPRESS A TO ATTACK\nDEFEAT NAVEC TO WIN",
                LEVEL_ONE_MESSAGE_POS, LEVEL_ONE_MESSAGE_POS);
    }

    private void displayLosingScreen() {

        // Displays centred "GAME OVER!" text
        titleName.drawString(GAME_LOSS, Window.getWidth()/2  - titleName.getWidth(GAME_LOSS)/2,
                Window.getHeight()/2);
    }
    private void displayTitleScreen() {

        // Displays the titles with its position on starting screen
        titleName.drawString(GAME_TITLE, startingTitlePosition.x, startingTitlePosition.y);

        // Displays the message beneath title on the starting screen
        titleMessage.drawString("PRESS SPACE TO START\nUSE ARROW KEYS TO FIND GATE",
                                 messagePosition.x, messagePosition.y);
    }

    /* this function determines if a player has won by checking
    if their coordinates match with the winning coordinates */
    private boolean gateReached(Player player){

        return player.getPlayerX() >= WINNING_COORDINATES_X && player.getPlayerY() >= WINNING_COORDINATES_Y;
    }
    /**
     * Performs a state update.
     * allows the game to exit when the escape key is pressed.
     */
    @Override
    protected void update(Input input) {
        // counts how many frames have passed since program has begun running
        counter++;

        if (input.wasPressed(Keys.ESCAPE)){
            Window.close();
        }


        if(!hasStarted){
            displayTitleScreen();
            if (input.wasPressed(Keys.SPACE)){
                hasStarted = true;
                isLevelZero = true;
            }
            if (input.wasPressed(Keys.K)) {
                hasStarted = true;
                isLevelOne = true;
                levelCompletedScreen = false;
                readCSV(LEVEL_ONE_CSV);
            }
        }
        if (levelCompletedScreen){
            // displays level completed message for 3000 milliseconds
            if (((REFRESH_RATE/MILLI_SECONDS) * LEVEL_COMPLETE_DELAY) >= counter - initialFrameCount){
                displayLevelComplete();
            }
            else if (input.wasPressed(Keys.SPACE)){
                isLevelOne = true;
                levelCompletedScreen = false;
                // loads csv file
                readCSV(LEVEL_ONE_CSV);
            }
            else {
                displayLevelOneTitleScreen();
            }
        }


        // toggles game screen on, loads in the background, objects, the player and health bar
        if (isLevelZero) {
            displayLevelZero();

            // draws the objects and checks if player interacts with them
            updateObjects();
            Fae.update(input, this);

            // delegate this to player
//            HealthBar healthBar = new HealthBar(Fae.getCurrentHealth(), Fae.getMaxHealth());
//            healthBar.drawHealthBar();

            if (gateReached(Fae)) {
                if (initialFrameCount == 0) {
                    initialFrameCount = counter;
                }
                isLevelZero = false;
                levelCompletedScreen = true;
            }
        }

        if (isLevelOne) {

            displayLevelOne();
            updateObjects();
            Fae.update(input, this);
            if (Fae.isAttacking()){
                System.out.println("FaE IS ATtacking");
                // check enemy method
                if (checkAttack(Fae) != null) {
                    if (!checkAttack(Fae).isInvincible()) {
                        checkAttack(Fae).loseHealth(Fae.getDamage());
                    }
                    checkAttack(Fae).setInvincible();

                    System.out.println(checkAttack(Fae).getPosition());
                }
            }

        }

        // character has lost the game, once they have lost all health
        if (Fae.getCurrentHealth() <= Fae.getMinHealth()) {
            isLevelZero = false;
            isLevelOne = false;
            losingScreen = true;
            displayLosingScreen();
        }

    }
    // returns enemy that has been attacked
    public DemonEnemy checkAttack(Entity entity) {
        Rectangle entityBox = entity.getBoundingBox();
        // add if statement for player
        for (DemonEnemy current : listOfEnemies) {
            if (entityBox.intersects(current.getBoundingBox())) {
                return current;
            }
        }
        return null;
    }
    // imported from project1 solutions
    public void checkCollisions(Entity entity) {

        Rectangle entityBox = entity.getBoundingBox();

        for (Wall current : listOfWalls) {
            Rectangle wallBox = current.getBoundingBox();
            if (entityBox.intersects(wallBox)) {
                entity.moveBack();
            }
        }

        for (Tree current : listOfTrees) {
            Rectangle treeBox = current.getBoundingBox();
            if (entityBox.intersects(treeBox)) {
                entity.moveBack();
            }
        }

        for (Sinkhole sinkhole : listOfSinkholes) {
            // only draws sinkholes that haven't been hit by player
            if (!sinkhole.isDeleted()) {

                // rebounds non-players when intersecting with sinkholes (causes no damage)
                if (entityBox.intersects(sinkhole.getBoundingBox()) && !(entity instanceof Player)) {
                    entity.moveBack();
                }
                // a player is damaged if they hit a sinkhole
                else if (entityBox.intersects(sinkhole.getBoundingBox()) == true) {
                        // FIX THIS CHANGE TO ENTITY
                    Fae.loseHealth(SINKHOLE_DAMAGE);
                    sinkhole.setDeleted();

                    System.out.println("Sinkhole inflicts " + SINKHOLE_DAMAGE + " damage on Fae.");
                    System.out.print(" Fae's current health: " + Fae.getCurrentHealth() + "/" +
                    Fae.getMaxHealth());



                    }
            }
        }
    }

    public void checkOutOfBounds(Entity entity){
        Point currentPosition = entity.getPosition();
        if ((currentPosition.y > gameWall.getBottomY()) || (currentPosition.y < gameWall.getTopY())
                || (currentPosition.x < gameWall.getLeftX()) || (currentPosition.x > gameWall.getRightX())){
            entity.moveBack();
        }



    }
}
