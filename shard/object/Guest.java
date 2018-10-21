/**
 * Guests mill around the mansion getting killed by the murderer (who is also
 * a Guest).
 *
 * @author Michael Bianconi
 * @author https://www.github.com/Michael-Bianconi
 */

package shard.object;

import java.util.ArrayList;
import java.util.Objects;

public class Guest extends ShardObject implements Owner {

    // MEMBER VARIABLES ========================================================
    private ArrayList<ShardObject> inventory;
    private boolean dead;
    private boolean murderer;


    // CONSTRUCTOR =============================================================

    /**
     * Name-only constructor. Calls this(name, name, null).
     * @param name Name of the object.
     */
    public Guest(String name) {
        this(name, name, null);
    }


    /**
     * Name and description constructor. Constructs with null location.
     * @param name Name of this object.
     * @param description Description of this object.
     */
    public Guest(String name, String description) {
        this(name, description, null);
    }


    /**
     * Name and location constructor. Constructs with description set to name.
     * @param name Name of the object.
     * @param location Location of the object.
     */
    public Guest(String name, Owner location) {
        this(name, name, location);
    }

    /**
     * Main constructor for Guest.
     * If description is omitted, replace it with name.
     * If location is omitted, use an empty Room.;
     * @param name Name of the Guest (e.g. Tom Hanks).
     * @param description Description of the Guest.
     * @param location Location of the Guest.
     */
    public Guest(String name, String description, Owner location) {
        super(name, description, location);
        this.inventory = new ArrayList<ShardObject>();
        this.dead = false;
        this.murderer = false;
    }

    // ACCESSORS ===============================================================

    /** Return a List of the objects held by this Guest. */
    public ArrayList<ShardObject> getObjects() { return inventory; }

    /** Add an object to this Guest's inventory. */
    public void addObject(ShardObject o) { inventory.add(o); }

    /** Remove an object from this Guest's inventory. */
    public void removeObject(ShardObject o) { inventory.remove(o); }

    /** Check if the Guest has an object in their inventory. */
    public boolean hasObject(ShardObject o) { return inventory.contains(o); }

    /** True if this person is dead. */
    public boolean isDead() { return dead; }

    /** Sets this Guest either to dead or alive. */
    public void setDead(boolean b) { dead = b; }

    /** Returns whether or not this guest is the murderer. */
    public boolean isMurderer() { return murderer; }

    /** Set whether or not this guest is the murderer. */
    public void setIsMurderer(boolean b) { murderer = b; }
}