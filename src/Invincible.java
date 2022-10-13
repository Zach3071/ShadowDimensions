/** This class is sued by entities that can become invincible */

public interface Invincible {
    boolean isInvincible();
    /** This sets the entity's isInvincible value to true */
    void triggerInvincibility();
    /** This renders the objects as invincible for a certain amount of time */
    void renderInvincibility(LevelManager gameObject);
}
