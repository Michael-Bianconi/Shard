/**
 * Shard
 * @author Michael Bianconi
 *
 * The world is comprised of several Rooms. Each room has a name, description,
 * and collection of ShardObjects. They also have a list of connected Rooms.
 * A room's location always points to itself.
 *
 * Note: A room is identified by its name. Don't have two rooms with the
 * same name.
 */
package shard.object;

import java.util.ArrayList;

public class Room implements ShardObject, Owner {

    private String name;
    private String description;
    private ArrayList<ShardObject> objects;
    private ArrayList<Room> connectedRooms;

    // CONSTRUCTORS ============================================================

    public Room(String name) {
        this.name = name;
        this.description = name;
        this.objects = new ArrayList<ShardObject>();
        this.connectedRooms = new ArrayList<Room>();
    }

    /**
     * Constructor for NullRooms. NullRooms are just like normal rooms (and
     * can be used as such), but are meant to be used as simple storage Owners.
     * @return A Null Room.
     */
    public static Room NullRoom() {
        Room nullroom = new Room("NULLROOM");
        nullroom.setDescription("A room that doesn't exist.");
        return nullroom;
    }

    // ACCESSORS ===============================================================
    public String getName() { return this.name; }
    public String getDescription() { return this.description; }
    public Owner getLocation() { return (Owner) this; }
    public ArrayList<ShardObject> getObjects() { return this.objects; }
    public ArrayList<Room> getConnectedRooms() { return this.connectedRooms; }
    
    public void setDescription(String d) { this.description = d; }
    public void addObject(ShardObject o) { this.objects.add(o); }
    public void removeObject(ShardObject o) { this.objects.remove(o); }
    public boolean hasObject(ShardObject o) { return this.objects.contains(o); }
    public void connectRoom(Room r) { this.connectedRooms.add(r); }

    // OVERRIDDEN METHODS ======================================================
    @Override
    public String toString() { return this.name; }

    /**
     * Check by comparing names.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (!(o instanceof Room)) { return false; }

        Room r = (Room) o;

        return this.name.equals(r.name);
    }

    @Override
    public int hashCode() { return this.name.hashCode(); }
}