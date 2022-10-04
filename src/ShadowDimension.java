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
    private final static int WINDOW_WIDTH = 1024;
    private final static int WINDOW_HEIGHT = 768;
    private final static int DEFAULT_FONT_SIZE = 75;
    private final static int MESSAGE_FONT_SIZE = 40;
    private final static int STARTING_TITLE_POSITION_X = 260;
    private final static int STARTING_TITLE_POSITION_Y = 250;
    private final static int MESSAGE_POSITION_X = STARTING_TITLE_POSITION_X + 90;
    private final static int MESSAGE_POSITION_Y = STARTING_TITLE_POSITION_Y + 190;


    // font object used for big default titles, "CONGRATULATIONS!, GAME OVER! etc.
    Font titleMessage = new Font("res/frostbite.ttf", MESSAGE_FONT_SIZE);
    Font titleName = new Font("res/frostbite.ttf", DEFAULT_FONT_SIZE);
    Point startingTitlePosition = new Point(STARTING_TITLE_POSITION_X, STARTING_TITLE_POSITION_Y);
    Point messagePosition = new Point(MESSAGE_POSITION_X, MESSAGE_POSITION_Y);

    // main game messages displayed
    private final static String GAME_TITLE = "SHADOW DIMENSION";
    private final static String GAME_WIN = "CONGRATULATIONS!";
    private final static String GAME_LOSS = "GAME OVER!";
    private final Image BACKGROUND_IMAGE = new Image("res/background0.png");

    // game screen boolean values
    private boolean startScreen = true;
    private boolean gameScreen = false;
    private boolean winningScreen = false;
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
    Boundary gameWall = new Boundary();


    public ShadowDimension(){
        super(WINDOW_WIDTH, WINDOW_HEIGHT, GAME_TITLE);
        readCSV();
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
    private void readCSV(){

        String line;

        // corresponds to csv file's columns
        final int OBJ_COLUMN = 0;
        final int COLUMN_X = 1;
        final int COLUMN_Y = 2;

        try {
            BufferedReader br = new BufferedReader(new FileReader("res/level0.csv"));
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
            wall.drawWall();
        }
    }

    // draws the background image of the game
    private void displayGameScreen() {
        BACKGROUND_IMAGE.draw(Window.getWidth() / 2.0, Window.getHeight() / 2.0);
    }

    private void displayWinningScreen() {

        // Displays centred "CONGRATULATIONS!" text
        titleName.drawString(GAME_WIN, Window.getWidth()/2 - titleName.getWidth(GAME_WIN)/2, Window.getHeight()/2);
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
    private boolean hasPlayerWon(Player player){

        return player.getPlayerX() >= WINNING_COORDINATES_X && player.getPlayerY() >= WINNING_COORDINATES_Y;
    }
    /**
     * Performs a state update.
     * allows the game to exit when the escape key is pressed.
     */
    @Override
    protected void update(Input input) {

        // this variable is set to true in readCSV() if a player has collided with another rectangle barrier
        barrierHit = false;

        // toggles off the title screen
        if (input.wasPressed(Keys.SPACE)) {
            startScreen = false;
            gameScreen = true;
        }

        // displays the starting screen, also ensures nothing else is loaded
        if (startScreen) {
            displayTitleScreen();
        }

        // toggles game screen on, loads in the background, objects, the player and health bar
        if (gameScreen) {
            displayGameScreen();

            // draws the objects and checks if player interacts with them
            updateObjects();
            Fae.update(input, this);
            HealthBar healthBar = new HealthBar(Fae.getCurrentHealth(), Fae.getMaxHealth());
            healthBar.drawHealthBar();
        }

        // triggers game winning screen
        if (hasPlayerWon(Fae)) {
            gameScreen = false;
            winningScreen = true;
            displayWinningScreen();
        }

        // character has lost the game, once they have lost all health
        if (Fae.getCurrentHealth() <= Fae.getMinHealth()) {
            gameScreen = false;
            losingScreen = true;
            displayLosingScreen();
        }

        if (input.wasPressed(Keys.ESCAPE)){
            Window.close();
        }
    }

    // imported from project1 solutions
    public void checkCollisions(Player player) {
        Rectangle faeBox = new Rectangle(player.getPosition(), player.getCurrentImage().getWidth(),
                player.getCurrentImage().getHeight());
        for (Wall current : listOfWalls) {
            Rectangle wallBox = current.getBoundingBox();
            if (faeBox.intersects(wallBox)) {
                player.moveBack();
            }
        }
    }

    public void checkOutOfBounds(Player player){
        Point currentPosition = player.getPosition();
        if ((currentPosition.y > gameWall.getBottomY()) || (currentPosition.y < gameWall.getTopY())
                || (currentPosition.x < gameWall.getLeftX()) || (currentPosition.x > gameWall.getRightX())){
            player.moveBack();
        }


        for (Sinkhole sinkhole : listOfSinkholes) {
            // only draws sinkholes that haven't been hit by player
            if (!listOfSinkholesDeleted.contains(sinkhole.getSinkholePosition())) {
                sinkhole.drawSinkhole();

                // player is damaged if they hit sinkhole
                if (Fae.getBoundingBox().intersects(sinkhole.getBoundingBox()) == true) {

                    Fae.loseHealth(SINKHOLE_DAMAGE);
                    listOfSinkholesDeleted.add(sinkhole.getSinkholePosition());

                    System.out.println("Sinkhole inflicts " + SINKHOLE_DAMAGE + " damage on Fae.");
                    System.out.print(" Fae's current health: " + Fae.getCurrentHealth() + "/" +
                            Fae.getMaxHealth());
                }
            }
        }
    }
}
