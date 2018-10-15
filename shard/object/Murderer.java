package shard.object;

/**
 * @author Michael-Bianconi
 * @author https://www.github.com/Michael-Bianconi
 * @version 1
 * @since 15 October 2018
 */

public class Murderer extends Guest {

    // constructors ============================================================

    public Murderer(String name) {
        super(name, name, null);
    }

    public Murderer(String name, String description) {
        super(name, description, null);
    }

    public Murderer(String name, Owner location) {
        super(name, name, location);
    }

    /**
     * Main constructor for Guest.
     * If description is omitted, replace it with name.
     * If location is omitted, use an empty Room.;
     * @param name Name of the Guest (e.g. Tom Hanks).
     * @param description Description of the Guest.
     * @param location Location of the Guest.
     */
    public Murderer(String name, String description, Owner location) {
        super(name, description, location);
    }

    // accessors ===============================================================


}