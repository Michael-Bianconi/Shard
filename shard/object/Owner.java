/**
 * Shard
 * @author Michael Bianconi
 *
 * The Owner interface allows Shard to place and retrieve Items into
 * that class. It is meant to be used with the Person and Room classes.
 */
package shard;

public interface Owner {
    public void addItem(Item i);
    public Item removeItem(Item i);
    public boolean hasItem(Item i);
    public Item getItem(Item i);
}