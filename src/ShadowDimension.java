import bagel.*;
import bagel.util.Point;


/**
 * Skeleton Code for SWEN20003 Project 1, Semester 2, 2022
 * Please enter your name below
 * @ ZACHARY KLIMAS, 1271087
 */

public class ShadowDimension extends AbstractGame {

    // window, title and message sizes
    /** This is the refresh rate used to update the game */
    public final static float REFRESH_RATE = 60;
    /** Milliseconds in a second */
    public final static float MILLI_SECONDS = 1000;
    /** This is the amount of frames that have passed since
     * the program has begun running
     */
    public static int frameCounter = 0;
    private final static int WINDOW_WIDTH = 1024;
    private final static int WINDOW_HEIGHT = 768;
    private final static int DEFAULT_FONT_SIZE = 75;
    private final static int MESSAGE_FONT_SIZE = 40;
    private final static int STARTING_TITLE_POSITION_X = 260;
    private final static int STARTING_TITLE_POSITION_Y = 250;
    private final static int MESSAGE_POSITION_X = STARTING_TITLE_POSITION_X + 90;
    private final static int MESSAGE_POSITION_Y = STARTING_TITLE_POSITION_Y + 190;


    // font object used for big default titles, "CONGRATULATIONS!", "GAME OVER!" etc.
    private Font titleMessage = new Font("res/frostbite.ttf", MESSAGE_FONT_SIZE);
    private Font titleName = new Font("res/frostbite.ttf", DEFAULT_FONT_SIZE);
    private Point startingTitlePosition = new Point(STARTING_TITLE_POSITION_X, STARTING_TITLE_POSITION_Y);
    private Point messagePosition = new Point(MESSAGE_POSITION_X, MESSAGE_POSITION_Y);

    // main game messages displayed
    private final static String GAME_TITLE = "SHADOW DIMENSION";
    private final static String GAME_WIN = "CONGRATULATIONS!";
    private final static String GAME_LOSS = "GAME OVER!";


    private final static int LEVEL_UNDEFINED = -1;
    private final static int FIRST_LEVEL = 0;
    private boolean hasStarted = false;

    private LevelManager levelManager;

    public ShadowDimension(){
        super(WINDOW_WIDTH, WINDOW_HEIGHT, GAME_TITLE);
        // Creates level manager which renders object's behaviours
        levelManager = new LevelManager(LEVEL_UNDEFINED);
    }

    /**
     * The entry point for the program.
     */
    public static void main(String[] args) {
        ShadowDimension game = new ShadowDimension();
        game.run();
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


    /**
     * Performs a state update.
     * allows the game to exit when the escape key is pressed.
     * Accesses the levels class to update objects for each level
     */
    @Override
    protected void update(Input input) {
        // counts how many frames have passed since program has begun running
        frameCounter++;

        if (input.wasPressed(Keys.ESCAPE)) {
            Window.close();
        }

        // starting screen
        if (!hasStarted) {
            displayTitleScreen();
            if (input.wasPressed(Keys.SPACE)) {
                hasStarted = true;
                levelManager.setLevel(FIRST_LEVEL);
            }
        }
        // continues rendering current level as long as player hasn't died or won
        if (!levelManager.isGameWon() && !levelManager.isGameover()) {
            levelManager.loadCurrentLevel(input);
        }
        if (levelManager.isGameover()) {
            displayLosingScreen();
        }
        if (levelManager.isGameWon()) {
            displayWinningScreen();
        }

    }

    public float getRefreshRate() { return REFRESH_RATE; }
    public float getMilliSeconds() { return MILLI_SECONDS;}
    public float getCurrentFrameCount() { return frameCounter; }
}
