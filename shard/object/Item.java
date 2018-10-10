/**
 * Shard
 *
 * Every object in Shard is an Item (not to be confused with Java's Object).
 */

package shard.object;

import java.util.Objects; // hashing


public class Item implements ShardObject {

    private String name;
    private String description;
    private ShardObject location;
    private ItemState state;

    /**
     * Most basic constructor. Initializes with NORMAL state.
     * @param name Name of the item.
     * @param location Location of the item (a Room or Person).
     */
    public Item(String name, ShardObject location) {
        this.name = name;
        this.location = location;
        this.state = ItemState.NORMAL;
        this.description = this.state.name().toLowerCase() + " " + this.name;
    }

    // ACCESSORS ===============================================================
    public String getName() { return this.name; }
    public ItemState getItemState() { return this.state; }
    public ShardObject getLocation() { return this.location; }
    public String getRawDescription() { return this.description; }
    public String getDescription() {
        String ret = this.description;

        if (this.state != ItemState.NORMAL) {
            ret += " It's " + this.state.name().toLowerCase();
        }

        return ret;
    }

    public void setDescription(String d) { this.description = d; }
    public void setState(ItemState s) { this.state = s; }
    public void setLocation(ShardObject o) { this.location = o; }

    // OVERRIDE METHODS ========================================================
    @Override
    public String toString() { return "a " + state.name() + " " + name; }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (!(o instanceof Item)) { return false; }
        Item i = (Item) o;

        return this.getDescription().equals(i.getDescription())
            && this.location.equals(i.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getDescription(), this.location);
    }
}