/**
 * Shard
 * @author Michael Bianconi
 */

package shard.object;

import java.util.ArrayList;
import java.util.Objects;

public class Player extends ShardObject implements Owner {

    // MEMBER VARIABLES ========================================================

    private ArrayList<ShardObject> inventory;

    // CONSTRUCTOR =============================================================

    public Player(String name) {
        this(name, name, new Room());
    }

    public Player(String name, String description) {
        this(name, description, new Room());
    }

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


    public ArrayList<ShardObject> getObjects() { return inventory; }
    public void addObject(ShardObject o) { inventory.add(o); }
    public void removeObject(ShardObject o) { inventory.remove(o); }
    public boolean hasObject(ShardObject o) { return inventory.contains(o); }
}