/**
 * Shard
 * @author Michael Bianconi
 *
 * A ShardObject represents everything in the Shard universe, similar to how
 * Object represents everything in Java.
 */
package shard.object;

public interface ShardObject {

    /**
     * Name of the object.
     */
    public String getName();

    /**
     * Basic description of the object.
     */
    public String getDescription();

    /**
     * Location of the object (should be a Room or Person).
     */
    public Owner getLocation();
}