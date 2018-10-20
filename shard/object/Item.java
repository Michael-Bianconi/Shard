/**
 * Shard
 *
 * Every object in Shard is an Item (not to be confused with Java's Object).
 *
 * Some items can be interacted with. This creates an Event. Items can daisy
 * chain uses. If an item is used without that usage being initialized,
 * an Error Event will be used.
 */

package shard.object;

import java.util.Set;
import java.util.HashSet;
import shard.event.*;

public class Item extends ShardObject {

    // MEMBER VARIABLES ========================================================

    private boolean weapon; // can be used as a weapon
    private boolean movable; // prevents being moved through events, can still
                             // be relocated
    private boolean usable;   // allows this object to be interacted with
    private boolean oneTimeUse; // may only be interacted with once
    private Command onUseCommand; // the Command that is performed when used
    private ShardObject onUseObject;

    // CONSTRUCTORS ============================================================

    /**
     * The main constructor for ShardObjects.
     * If the description is omitted, it is replaced by the given name.
     * If the location is omitted, Room.NullRoom() is used.
     * @param name Name of the object (try to make it unique).
     * @param description Description of the object (objects with the same
     *                    name should have the same description but it's not
     *                    required).
     * @param location Location of the object.
     */
    public Item(String name, String description, Owner location) {
        super(name, description, location);
        usable = false;
        oneTimeUse = false;
        movable = false;
        onUseCommand = Command.ERROR;
        onUseObject = null;

    }

    // ACCESSORS ===============================================================

    /** This can by used by the murderer as a weapon. */
    public boolean getWeapon() { return this.weapon; }

    /** This item can by used by the Player, Murderer, and Guests. */
    public boolean getUsable() { return this.usable; }

    /**
     * This item can be dropped or taken. Does not prevent setLocation() itself
     * from being called. Only affect Command.TAKE and Command.DROP candidate
     * lists.
     */
    public boolean getMovable() { return this.movable; }

    /** Once used, this item becomes unusable. */
    public boolean getOneTimeUse() { return this.oneTimeUse; }

    /** The Event Command this object returns. */
    public Command getOnUseCommand() { return this.onUseCommand; }

    /** The object this Item interacts with. */
    public ShardObject getOnUseObject() { return this.onUseObject; }

    /** Set this object to be a weapon. */
    public void setWeapon(boolean b) { this.weapon = b; }

    /** Set this object to be usable. Default false. */
    public void setUsable(boolean b) { this.usable = b; }

    /** Once used, set usable to false (done automatically). Default false. */
    public void setOneTimeUse(boolean b) { this.oneTimeUse = b; }

    /** This is a candidate to for DROP and TAKE commands. Default false. */
    public void setMovable(boolean b) { this.movable = b; }

    /** The Command used in this Item's use Event. Default ERROR. */
    public void setOnUseCommand(Command c) { this.onUseCommand = c; }

    /** The object this Item uses in it's use Event. Default null. */
    public void setOnUseObject(ShardObject o) { this.onUseObject = o; }

    /** Calls setOnUseCommand() and setOnUseObject() */
    public void setOnUseEvent(Command c, ShardObject o) {
        setOnUseCommand(c);
        setOnUseObject(o);
    }

    /**
     * Creates an event executed by executor. If this oneTimeUse is true, set
     * usable false.
     * @param executor executes event
     */
    public Event use(ShardObject executor) {

        if (usable) {

            if (oneTimeUse) { usable = false; }

            return new Event(onUseCommand, executor, onUseObject);
        }

        return new Event(Command.ERROR, executor, onUseObject);
    }


    // OVERRIDE METHODS ========================================================

}