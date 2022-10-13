import bagel.DrawOptions;
import bagel.Font;

/** This class is usually implemented by entitys
 * that contain a health attribute
 */
public class HealthBar {

    private double currentHealthPoints;
    private double maximumHealthPoints;
    private final double ORANGE_HEALTH = 65;
    private final double RED_HEALTH = 35;

    private DrawOptions colour = new DrawOptions();


    HealthBar(double currentHealthPoints, double maximumHealthPoints){
        this.currentHealthPoints = currentHealthPoints;
        this.maximumHealthPoints = maximumHealthPoints;
    }


    /** This renders a health bar with text displaying
     * the percentage of its currentHealth from maximumHealth,
     * the colours change depending on the percentage of health
     * @param xPosition This is the x coordinate of the health bar
     * @param yPosition This is the y coordinate of the health bar
     * @param fontSize This is the font size of the text displayed from the health bar
     * @param currentHealthPoints This is the current health of the entity
     * @param maximumHealthPoints This is the maximum health of the entity
     */
    public void drawHealthBar(double xPosition, double yPosition, int fontSize,
                              double currentHealthPoints, double maximumHealthPoints){
        double healthPercent = currentHealthPoints / maximumHealthPoints * 100;
        int roundedHealthPercent = (int) (healthPercent + 0.5);

        Font healthBarFont = new Font("res/frostbite.ttf", fontSize);

        // If between 65 and 100 health percentage, set health bar to green
        colour.setBlendColour(0, 0.8, 0.2);

        // If between 35 and 65 health percentage, set health bar to orange
        if (healthPercent < ORANGE_HEALTH){
            colour.setBlendColour(0.9, 0.6, 0);

        }

        // If between 35 and 65 health percentage, set health bar to red
        if (healthPercent < RED_HEALTH) {
            colour.setBlendColour(1, 0, 0);
        }

        // Displays the health bar
        healthBarFont.drawString(Integer.toString(roundedHealthPercent) + "%", xPosition, yPosition, colour);
    }
}
