/**
 * Controlled by the user.
 *
 * @author Michael Bianconi
 * @author https://www.github.com/Michael-Bianconi
 * @version 1
 */

package shard.object;

import java.util.ArrayList;
import java.util.Objects;

public class Player extends ShardObject implements Owner {

    // MEMBER VARIABLES ========================================================

    private ArrayList<ShardObject> inventory;

    // CONSTRUCTOR =============================================================

    /**
     * Name-only constructor, constructs Player in an empty room with
     * description set to name.
     * @param name Name of the player.
     */
    public Player(String name) {
        this(name, name, new Room());
    }


    /**
     * Name and description constructor, constructs player in an empty room.
     * @param name Name of the player.
     * @param description Description of the player.
     */
    public Player(String name, String description) {
        this(name, description, new Room());
    }

    /**
     * Name and location constructor, constructs with description set to name.
     * @param name Name of the player.
     * @param location Location of the player.
     */
    public Player(String name, Owner location) {
        this(name, name, location);
    }

    /**
     * Main constructor for Person.
     * If description is omitted, replace it with name.
     * If location is omitted, use an empty Room.;
     * @param name Name of the person (e.g. Tom Hanks).
     * @param description Description of the person.
     * @param location Location of the person.
     */
    public Player(String name, String description, Owner location) {
        super(name, description, location);
        this.inventory = new ArrayList<ShardObject>();
    }

    // ACCESSORS ===============================================================

    /**
     * Gets an ArrayList of the objects in this player's inventory.
     * @return Returns an ArrayList of the ShardObjects.
     */
    public ArrayList<ShardObject> getObjects() { return inventory; }

    /**
     * Adds an object to this player's inventory.
     * @param o Object to add.
     */
    public void addObject(ShardObject o) { inventory.add(o); }

    /**
     * Removes an object from this player's inventory.
     * @param o Object to remove.
     */
    public void removeObject(ShardObject o) { inventory.remove(o); }

    /**
     * Checks if the given object is in this player's inventory.
     * @param o Object to check.
     * @return Returns true if o is in this player's inventory.
     */
    public boolean hasObject(ShardObject o) { return inventory.contains(o); }
}