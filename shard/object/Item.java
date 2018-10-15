/**
 * Shard
 *
 * Every object in Shard is an Item (not to be confused with Java's Object).
 */

package shard.object;

import java.util.Set;
import java.util.HashSet;

public class Item extends ShardObject implements Tagged {

    // MEMBER VARIABLES ========================================================

    Set<Tag> tags = new HashSet<Tag>();


    // CONSTRUCTORS ============================================================

    public Item(String name) {
        this(name, name, null);
    }

    public Item(String name, String description) {
        this(name, description, null);
    }

    public Item(String name, Owner location) {
        this(name, name, location);
    }

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
    }

    // ACCESSORS ===============================================================
    public Set<Tag> getTags() { return this.tags; }
    public void addTag(Tag t) { this.tags.add(t); }
    public void removeTag(Tag t) { this.tags.remove(t); }
    public boolean hasTag(Tag t) { return this.tags.contains(t); }

    // OVERRIDE METHODS ========================================================

}