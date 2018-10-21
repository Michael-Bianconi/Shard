/**
 * The world is comprised of several Rooms. Each room has a name, description,
 * and collection of ShardObjects. They also have a list of connected Rooms.
 * A room's location always points to itself.
 *
 * Note: A room is identified by its name. Don't have two rooms with the
 * same name.
 */
package shard.object;

import java.util.ArrayList;

public class Room extends ShardObject implements Owner {

    private ArrayList<ShardObject> objects;
    private ArrayList<Room> connectedRooms;

    // CONSTRUCTORS ============================================================
    
    /**
     * Name-only constructor, constructs with description set to name.
     * @param name Name of the room.
     */
    public Room(String name) {
        this(name, name);
    }

    /**
     * Main constructor for Room.
     * If description is omitted, replace it with name.
     * @param name Name of the room (e.g. "lounge").
     * @param description Description of the room.
     */
    public Room(String name, String description) {
        super(name, description, null);
        this.objects = new ArrayList<ShardObject>();
        this.connectedRooms = new ArrayList<Room>();
    }

    /**
     * Constructor for empty Rooms. Empty Rooms are just like normal rooms (and
     * can be used as such), but are meant to be used as simple storage Owners.
     */
    public Room() {
        this("room", "a non-descript room");
    }

    // ACCESSORS ===============================================================

    /**
     * Gets all ShardObjects in this room.
     * @return ArrayList of ShardObjects in this room.
     */
    public ArrayList<ShardObject> getObjects() { return this.objects; }

    /**
     * Gets all rooms connected to this one.
     * @return ArrayList of all connected rooms.
     */
    public ArrayList<Room> getConnectedRooms() { return this.connectedRooms; }
    
    /**
     * Adds the object to this room.
     * @param o Object to add.
     */
    public void addObject(ShardObject o) { objects.add(o); }

    /**
     * Removes the object from this room.
     * @param o Object to remove.
     */
    public void removeObject(ShardObject o) { objects.remove(o); }

    /**
     * Checks if an object is in this room.
     * @param o Object to check.
     * @return True if this room contains this object.
     */
    public boolean hasObject(ShardObject o) { return objects.contains(o); }

    /**
     * Connect another room to this one.
     * @param r Room to connect.
     * @see Room.connectRooms()
     */
    public void connectRoom(Room r) { connectedRooms.add(r); }

    /**
     * Use this method to connect both the rooms together and make a
     * bidirectional connection.
     *
     * Using connectRoom(Room) makes a unidirectional connection (useful for
     * secret passages), although calling connectRoom() on both r1 and r2
     * achieves the same effect as this method.
     * @param r1 First room
     * @param r2 Second room
     * @see Room.connectRoom()
     */
    public static void connectRooms(Room r1, Room r2) {
        r1.connectRoom(r2);
        r2.connectRoom(r1);
    }
}