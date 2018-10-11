/**
 * Shard
 * @author Michael Bianconi
 */

package shard.object;

import java.util.ArrayList;

public class Player implements ShardObject, Owner {

    // MEMBER VARIABLES ========================================================

    private String name;
    private String description;
    private Owner location;
    private ArrayList<ShardObject> inventory;

    // CONSTRUCTOR =============================================================

    public Player(String name, Owner location) {
        this.name = name;
        this.description = name;
        this.location = location;
        this.location.addObject(this);
        this.inventory = new ArrayList<ShardObject>();
    }

    // ACCESSORS ===============================================================

    public String getName() { return this.name; }
    public String getDescription() { return this.description; }
    public Owner getLocation() { return this.location; }
    public ArrayList<ShardObject> getObjects() { return this.inventory; }

    public void setDescription(String d) { this.description = d; }
    public void addObject(ShardObject o) { this.inventory.add(o); }
    public void removeObject(ShardObject o) { this.inventory.remove(o); }
    public boolean hasObject(ShardObject o) { return this.inventory.contains(o); }
    public void setLocation(Owner l) {
        this.location.removeObject(this);
        this.location = l;
        this.location.addObject(this);
    }

    // OVERRIDEN METHODS =======================================================

    @Override
    public String toString() { return this.name; }

    @Override
    public int hashCode() { return this.name.hashCode(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (!(o instanceof Player)) { return false; }

        Player p = (Player) o;

        return this.name.equals(p.name);
    }
}