/**
 * Shard
 * @author Michael Bianconi
 *
 * Each Guest has an inventory that holds ShardObjects. Yes, that means
 * James can hold the kitchen in his pocket, but he probably shouldn't.
 *
 * When calling equals(), inventory isn't taken into account for reasons
 * outlined in the method documentation. That means don't differentiate
 * two Guests by giving one an apple and the other an orange.
 *
 * In fact, if you factor in user input, make sure to always give your Guests
 * unique names. The player can't distinguish between David Smith and David
 * Smith when he tells you "talk to David Smith".
 */

package shard.object;

import java.util.ArrayList;
import java.util.Objects;

public class Guest extends ShardObject implements Owner, Memory {

    // MEMBER VARIABLES ========================================================
    private ArrayList<ShardObject> inventory;
    private ArrayList<String> memories;


    // CONSTRUCTOR =============================================================

    public Guest(String name) {
        this(name, name, null);
    }

    public Guest(String name, String description) {
        this(name, description, null);
    }

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
        this.memories = new ArrayList<String>();
    }

    // ACCESSORS ===============================================================

    public ArrayList<ShardObject> getObjects() { return inventory; }
    public void addObject(ShardObject o) { inventory.add(o); }
    public void removeObject(ShardObject o) { inventory.remove(o); }
    public boolean hasObject(ShardObject o) { return inventory.contains(o); }

    public ArrayList<String> getMemories() { return memories; }
    public void addMemory(String m) { memories.add(m); }
    public String getLatestMemory() {
        if (memories.size() == 0) { return "I can't remember anything"; }
        else { return memories.get(memories.size() - 1); }
    }
}