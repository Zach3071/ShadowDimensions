import bagel.*;
import bagel.util.Point;
import bagel.util.Rectangle;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/** This class manages each level of the game and updates & renders
 * objects/conditions from each level */

public class LevelManager {
    private final static int DEFAULT_FONT_SIZE = 75;
    private final static int MESSAGE_FONT_SIZE = 40;
    private final static int LEVEL_ONE_MESSAGE_POS = 350;
    public boolean hasReadCurrentCSV;
    private final static String LEVEL_ZERO_CSV = "res/level0.csv";
    private final static String LEVEL_ONE_CSV = "res/level1.csv";
    Font titleMessage = new Font("res/frostbite.ttf", MESSAGE_FONT_SIZE);
    Font titleName = new Font("res/frostbite.ttf", DEFAULT_FONT_SIZE);
    private final Image LEVEL_ZERO_BACKGROUND = new Image("res/background0.png");
    private final Image LEVEL_ONE_BACKGROUND = new Image("res/background1.png");


    private int level;
    private static final int LEVEL_ONE = 1;
    private final static int LEVEL_ZERO = 0;
    private boolean playerInitialised;



    // game's outer wall boundary coordinates
    private double topLeftX;
    private double topLeftY;
    private double BottomRightX;
    private double BottomRightY;
    private final static double WINNING_COORDINATES_X = 950;
    private final static double WINNING_COORDINATES_Y = 670;
    private static final double SINKHOLE_DAMAGE = 30;

    // Objects loaded by each level
    private Player Fae = new Player();
    private List<Wall> listOfWalls = new ArrayList<>();
    private List<Sinkhole> listOfSinkholes = new ArrayList<>();
    private List<Tree> listOfTrees = new ArrayList<>();
    private List<DemonEnemy> listOfEnemies = new ArrayList<>();
    private Boundary gameWall = new Boundary();


    private boolean levelCompletedScreen;
    private int frameCountAtGate = 0;
    private final static double LEVEL_COMPLETE_DELAY = 3000;
    private final static String LEVEL_COMPLETE = "LEVEL COMPLETE!";

    private boolean gameWon;

    LevelManager(int level) {
        this.level = level;
        this.hasReadCurrentCSV = false;
        this.playerInitialised = false;
    }

    /** This loads the current level's objects using its corresponding CSV file,
     * and is used in the ShadowDimension update function.
     * It also updates and renders each object.
     * @param input This is input made by the user
     */
    public void loadCurrentLevel(Input input) {

        if (level == LEVEL_ZERO) {
            // loads CSV if has not been loaded
            if (!hasReadCurrentCSV) {
                readCSV(LEVEL_ZERO_CSV);
                Fae.setHealth(Fae.getMaxHealth());
                hasReadCurrentCSV = true;
            }
            displayLevelZero();
            Fae.update(input, this);
            updateObjects();

            if (gateReached(Fae)) {
                if (frameCountAtGate == 0) {
                    frameCountAtGate = ShadowDimension.frameCounter;
                }
                levelCompletedScreen = true;
                level++;
            }
        }

        if (levelCompletedScreen) {
            // displays level completed message for 3000 milliseconds
            if (((ShadowDimension.REFRESH_RATE / ShadowDimension.MILLI_SECONDS) *
                    LEVEL_COMPLETE_DELAY) >= ShadowDimension.frameCounter - frameCountAtGate) {
                displayLevelComplete();
            }
            else {
                displayLevelOneTitleScreen();
                if (input.wasPressed(Keys.SPACE)) {
                    levelCompletedScreen = !levelCompletedScreen;
                    hasReadCurrentCSV = false;
                }
            }
        }
        if (level == LEVEL_ONE && !levelCompletedScreen) {
            if (!hasReadCurrentCSV) {
                readCSV(LEVEL_ONE_CSV);
                Fae.setHealth(Fae.getMaxHealth());
                hasReadCurrentCSV = true;
            }
            displayLevelOne();
            Timescale.update(input);
            updateObjects();
            Fae.update(input, this);

            if (Fae.isAttacking()){
                for (DemonEnemy currentEnemy : listOfEnemies) {
                    // examines and attacks enemy if it is in player's attack range
                    if (!currentEnemy.isInvincible() && Fae.attack(currentEnemy)) {
                            currentEnemy.triggerInvincibility();
                    }
                }
            }
            Timescale.setTimescaleUpdated();
        }
    }
    public void setLevel(int level) { this.level = level; }

    private void displayLevelZero() {
        LEVEL_ZERO_BACKGROUND.draw(Window.getWidth() / 2.0, Window.getHeight() / 2.0);
    }
    private void displayLevelOne() {
        LEVEL_ONE_BACKGROUND.draw(Window.getWidth() / 2.0, Window.getHeight() / 2.0);
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

    /**
     * Method used to read CSV files and load objects
     * @param levelFileName This is the CSV file being loaded in
     *                      for a certain level
     */
    private void readCSV(String levelFileName){

        String line;

        // corresponds to csv file's columns
        final int OBJ_COLUMN = 0;
        final int COLUMN_X = 1;
        final int COLUMN_Y = 2;

        try {
            BufferedReader br = new BufferedReader(new FileReader(levelFileName));

            // Overwrites previous read CSV file
            listOfWalls.clear();
            listOfSinkholes.clear();
            listOfTrees.clear();
            listOfEnemies.clear();
            playerInitialised = false;

            while((line = br.readLine()) != null) {
                String[] row = line.split(",");

                // Creates player object and initialises starting position
                if (row[OBJ_COLUMN].equals("Fae") && !playerInitialised){
                    Fae.setPosition(Integer.parseInt(row[COLUMN_X]), Integer.parseInt(row[COLUMN_Y]));
                    playerInitialised = true;
                }
                if (row[OBJ_COLUMN].equals("Wall")) {
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
            // ensures that only alive demons are rendered and updated
            if (!demon.isDead()) {
                demon.update(this);


                if (demon.attack(Fae)) {
                    Fae.triggerInvincibility();
                }
            }
            if (demon.getClass().getName().equals("Navec") && demon.isDead()) {
                gameWon = true;
            }
        }
    }

    /** This method is used by entities to check if their bounding box collides
     * with certain objects
     * @param entity This is the entity being checked for collisions
     */
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
                else if (entityBox.intersects(sinkhole.getBoundingBox())) {
                    // FIX THIS CHANGE TO ENTITY
                    entity.loseHealth(SINKHOLE_DAMAGE);
                    sinkhole.setDeleted();

                    System.out.println("Sinkhole inflicts " + (int)SINKHOLE_DAMAGE +
                            " damage on Fae. Fae's current health: " +
                            (int)entity.getCurrentHealth() + "/" + (int)entity.getMaxHealth());
                }
            }
        }
    }

    /** This checks if an entity has collided with the wall boundary and
     * rebounds them back if true
     * @param entity This is the entity being checked for the collision
     */
    public void checkOutOfBounds(Entity entity) {
        Point currentPosition = entity.getPosition();
        if ((currentPosition.y > gameWall.getBottomY()) || (currentPosition.y < gameWall.getTopY())
                || (currentPosition.x < gameWall.getLeftX()) || (currentPosition.x > gameWall.getRightX())) {
            entity.moveBack();
        }
    }

    /** Checks if a player has reached level 1's portal gate
     * @param player This is the player that will have its coordinates checked
     * @return Returns true if player has reached the gate
     */
    public boolean gateReached(Player player){
        return player.getPlayerX() >= WINNING_COORDINATES_X && Fae.getPlayerY() >= WINNING_COORDINATES_Y;
    }
    /** Checks if the player has died */
    public boolean isGameover() { return Fae.isDead(); }
    /** Checks if the player has killed Navec */
    public boolean isGameWon() { return gameWon; }



}

