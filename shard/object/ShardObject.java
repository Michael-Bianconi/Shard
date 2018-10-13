/**
 * Shard
 * @author Michael Bianconi
 *
 * A ShardObject represents everything in the Shard universe, similar to how
 * Object represents everything in Java.
 *
 * The player specifies ShardObjects by name. Therefore, there should be no
 * chance of collision between the names of two ShardObjects.
 *
 * If two ShardObjects share the same name, they should be functionally
 * identical.
 */
package shard.object;

public class ShardObject {

    // REFID COUNTER ===========================================================
    private static int nextRefID = 0;

    // MEMBER VARIABLES ========================================================
    /** Name of the object. Try to make it unique. */
    private String name;

    /** Description of the object. */
    private String description;

    /** Location of the object.*/
    private Owner location;

    /** Unique ID of the object for equals() and hashCode(). */
    private final int refID;

    // CONSTRUCTORS ============================================================

    public ShardObject(String name) {
        this(name, name, null);
    }

    public ShardObject(String name, String description) {
        this(name, description, null);
    }

    public ShardObject(String name, Owner location) {
        this(name, name, location);
    }

    /**
     * The main constructor for ShardObjects.
     * If the description is omitted, it is replaced by the given name.
     * If the location is omitted, an empty Room is used.
     * @param name Name of the object (try to make it unique).
     * @param description Description of the object (objects with the same
     *                    name should have the same description but it's not
     *                    required).
     * @param location Location of the object.
     */
    public ShardObject(String name, String description, Owner location) {
        this.name = name;
        this.description = description;
        this.location = location;
        if ( this.location != null) { this.location.addObject(this); }
        this.refID = ShardObject.nextRefID++;
    }

    // ACCESSORS ===============================================================
    public String getName() { return this.name; }
    public String getDescription() { return this.description; }
    public Owner getLocation() { return this.location; }
    public int getRefID() { return this.refID; }

    /**
     * Set the location of this object.
     * @param owner Owner to set.
     */
    public void setLocation(Owner owner) {
        if (this.location != null) {
            this.location.removeObject(this);
        }
        this.location = owner;
        this.location.addObject(this);
    }

    // OBJECT METHODS ==========================================================
    @Override
    public String toString() { return this.name; }

    @Override
    public int hashCode() { return this.refID; }

    @Override
    public boolean equals(Object other) {
        if (this == other) { return true; }
        if (!(other instanceof ShardObject)) { return false; }
        ShardObject o = (ShardObject) other;

        return this.refID == o.refID;
    }
}