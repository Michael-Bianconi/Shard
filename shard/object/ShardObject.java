/**
 * A ShardObject represents everything in the Shard universe, similar to how
 * Object represents everything in Java.
 *
 * The player specifies ShardObjects by name. Therefore, there should be no
 * chance of collision between the names of two ShardObjects.
 *
 * @author Michael Bianconi
 * @author https://www.github.com/Michael-Bianconi
 * @version 1
 * @since 13 October 2018
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

    /**
     * Name-only constructor. Calls this(name, name, null).
     * @param name Name of the object.
     */
    public ShardObject(String name) {
        this(name, name, null);
    }


    /**
     * Name and description constructor. Constructs with null location.
     * @param name Name of this object.
     * @param description Description of this object.
     */
    public ShardObject(String name, String description) {
        this(name, description, null);
    }

    /**
     * Name and location constructor. Constructs with description set to name.
     * @param name Name of the object.
     * @param location Location of the object.
     */
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

    /**
     * Gets this object's name.
     * @return name
     */
    public String getName() { return this.name; }

    /**
     * Gets this object's description.
     * @return description
     */
    public String getDescription() { return this.description; }

    /**
     * Gets this object's location.
     * @return location
     */
    public Owner getLocation() { return this.location; }

    /**
     * Gets this object's refID.
     * @return refID
     */
    public int getRefID() { return this.refID; }

    /**
     * Set the location of this object. Will remove it from
     * it's current location.
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

    /**
     * Returns String representation of this object.
     * @return name
     */
    @Override
    public String toString() { return this.name; }

    /**
     * hashCode of this object.
     * @return refID
     */
    @Override
    public int hashCode() { return this.refID; }


    /**
     * Checks refID of this object and other.
     * @param other Other object to check.
     * @return True if matching refIDs.
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) { return true; }
        if (!(other instanceof ShardObject)) { return false; }
        ShardObject o = (ShardObject) other;

        return this.refID == o.refID;
    }
}