import bagel.Input;
import bagel.Keys;
/** This class contains the current timescale which affects the movement
 * of demon enemies*/
public class Timescale {
    private static final int MAX_TIMESCALE = 3;
    private static final int MIN_TIMESCALE = -3;
    private static int timescale = 0;
    private static boolean timescaleUpdated;

    /** This updates to see if the timescale has changed from the user
     * @param input Takes the user input to change the timescale
     */
    public static void update(Input input) {
        if (input.wasPressed(Keys.L)) {
            if (timescale != MAX_TIMESCALE) {
                timescale++;
                System.out.println("Sped up, Speed: " + timescale);
                timescaleUpdated = false;
            }
        } else if (input.wasPressed(Keys.K)) {
            if (timescale != MIN_TIMESCALE) {
                timescale--;
                System.out.println("Slowed down, Speed: " + timescale);
                timescaleUpdated = false;
            }
        }
    }
    public static int getTimescale() { return timescale; }
    public static void setTimescaleUpdated() { timescaleUpdated = true; }
    public static boolean hasTimescaleUpdated () { return timescaleUpdated; }
}
