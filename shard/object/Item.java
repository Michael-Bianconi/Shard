/**
 * Shard
 *
 * Every object in Shard is an Item (not to be confused with Java's Object).
 */

package shard.object;

public class Item extends ShardObject {

    // MEMBER VARIABLES ========================================================

    private ItemState state;

    // CONSTRUCTORS ============================================================

    public Item(String name) {
        this(name, name, null, ItemState.NORMAL);
    }

    public Item(String name, String description) {
        this(name, description, null, ItemState.NORMAL);
    }

    public Item(String name, Owner location) {
        this(name, name, location, ItemState.NORMAL);
    }

    public Item(String name, ItemState state) {
        this(name, name, null, state);
    }

    public Item(String name, String description, ItemState state) {
        this(name, description, null, state);
    }

    public Item(String name, Owner location, ItemState state) {
        this(name, name, location, state);
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
     * @param state This Item's ItemState.
     */
    public Item(String name,
                String description,
                Owner location,
                ItemState state) {
        super(name, description, location);
        this.state = state;
    }

    // ACCESSORS ===============================================================
    public ItemState getItemState() { return this.state; }
    public void setItemState(ItemState s) { this.state = s; }

    /**
     * If this Item's state is non-NORMAL, append the state to the end of
     * the description.
     * @return String
     */
    @Override
    public String getDescription() {
        String ret = this.getDescription();

        if (this.state != ItemState.NORMAL) {
            ret += " It's " + this.state.name().toLowerCase();
        }

        return ret;
    }

    // OVERRIDE METHODS ========================================================
    /**
     * Include the item's state if it's non-NORMAL.
     * @return String
     */
    @Override
    public String toString() {
        if (state == ItemState.NORMAL) { return super.toString(); }
        else { return state.name().toLowerCase() + " " + super.toString(); }
    }
}