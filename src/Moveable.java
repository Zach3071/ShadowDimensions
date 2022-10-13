/** This class enables entities to move */
public interface Moveable {
    /** Move's an entity to new coordinates
     * @param xMove This is the new x coordinate
     * @param yMove This is the new y coordinate
     */
    void move(double xMove, double yMove);

    /** This rebounds an entity usually from a collision,
     * functionality differs per entity
     */
    void moveBack();

}
