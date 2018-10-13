/**
 * Shard
 * @author Michael Bianconi
 *
 * Right now this manager is pretty janky. I'll come back and clean it up.
 * As of right now, it only accepts input consisting of a one-word command
 * and a single argument (although the argument may be several words).
 *
 * Additionally, the user is forced to use full names.
 *
 * Examples:
 *
 * Valid:
 *
 *      goto kitchen
 *      drop rope
 *      talk fred barnes
 *
 * Invalid:
 *
 *      go to kitchen
 *      drop rope (when only a bloody rope is in the player's inventory)
 *      talk to fred barnes
 *      talk fred
 */
package shard.user;
import shard.event.Event;
import shard.event.EventCode;
import shard.object.*;
import java.util.ArrayList;
import java.util.Arrays;

public class InputManager {

    // MEMBER VARIABLES ========================================================

    private Player player;

    // CONSTRUCTOR =============================================================

    public InputManager(Player p) {
        this.player = p;
    }

    // INPUT PARSING ===========================================================

    /**
     * Given a String command, ascertain its corresponding EventCode. Then, if
     * applicable, build the rest of the Event using information gleened from
     * this instance's Player. For example, if the command specifies to take
     * a knife, look around the Player's location for a knife.
     *
     * When identifying Items, it compares the toString() version of the item.
     */
    public Event parse(String input) throws CommandNotFoundException,
                                            ArgumentNotFoundException {

        if (input.length() == 0) { throw new CommandNotFoundException(
                                             "Empty command!"); }

        // arr[0] = command
        // arr[1] = argument
        String [] arr = input.split(" ", 2);
        String command = arr[0];
        String argument = (arr.length == 2) ? arr[1] : "";
        EventCode c;

        // Read in the event code or return an Error
        try {
            c = EventCode.valueOf(command.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CommandNotFoundException("Unknown command!");
        }

        ArrayList<ShardObject> objectList;

        // parse the command arguments
        try {
            // args should be limited to Rooms
            switch (c) {

                // Look for connected rooms
                case GOTO: // drop down to INVESTIGATE
                case INVESTIGATE: objectList = roomParse(argument);
                    break;

                // Look for Item held by the player
                case DROP: objectList = itemOnPlayerParse(argument);
                    break;

                // Look for Item in current room.
                case TAKE: objectList = itemInRoomParse(argument);
                    break;

                // Look for Guest in current room.
                case TALK: objectList = guestParse(argument);
                    break;

                // Look for anything
                case DESCRIBE: objectList = allObjectParse(argument);
                    break;

                // The user used ERROR as the command for some reason
                case ERROR: objectList = new ArrayList<ShardObject>();
                    break;

                // Default (should never happen)
                default:
                    throw new CommandNotFoundException("Dev error");
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new ArgumentNotFoundException("Argument not found!");
        }

        return new Event(c, this.player, objectList);
    }

    /**
     * Look through the given list of objects and match it to the given name.
     * Matches are determined through comparing the name to the ShardObject's
     * toString() method.
     *
     * Only ShardObject's that share the same class as the filter may be
     * matched.
     *
     * @param name Name to match.
     * @param filter Class to match.
     * @param objs Possible ShardObjects.
     * @return ArrayList of ShardObject.
     */
    private ArrayList<ShardObject> objectParse(String name,
                                               Class filter,
                                               ArrayList<ShardObject> objs)
                                          throws ArgumentNotFoundException {

        ArrayList<ShardObject> parsedArgs = new ArrayList<ShardObject>();
        
        // for each ShardObject in the supplied list
        for (ShardObject o : objs) {

            // if the object is of the correct type
            if (filter.equals(o.getClass())) {

                // check if their names are the same
                if (name.toUpperCase().equals(o.toString().toUpperCase())) {
                    parsedArgs.add(o);
                    return parsedArgs;
                }
            }
        }

        // no corresponding ShardObject was found.
        throw new ArgumentNotFoundException("Can't find " + name);
    }

    /** Call objectParse on the current room and all connected rooms. */
    private ArrayList<ShardObject> roomParse(String arg)
        throws ArgumentNotFoundException {

        ArrayList<ShardObject> list = new ArrayList<ShardObject>();

        if (player.getLocation() instanceof Room) {
            Room location = (Room) player.getLocation();
            list.add(location);
            list.addAll(location.getConnectedRooms());
        }

        return objectParse(arg, Room.class, list);
    }

    /** objectParse on all items in the player's inventory. */
    private ArrayList<ShardObject> itemOnPlayerParse(String arg)
        throws ArgumentNotFoundException {

        return objectParse(arg, Item.class, this.player.getObjects());
    }

    /** Call objectParse on all items in the current room. */
    private ArrayList<ShardObject> itemInRoomParse(String arg)
        throws ArgumentNotFoundException {

        return objectParse(arg, Item.class, player.getLocation().getObjects());
    }

    /** Call objectParse on all people in the current room. */
    private ArrayList<ShardObject> guestParse(String arg)
        throws ArgumentNotFoundException {

        return objectParse(arg, Guest.class, player.getLocation().getObjects());
    }

    /** Call objectParse on all ShardObjects on the player and in the room. */
    private ArrayList<ShardObject> allObjectParse(String arg)
        throws ArgumentNotFoundException {

        ArrayList<ShardObject> list = new ArrayList<ShardObject>();

        list.addAll(player.getObjects());

        if (player.getLocation() instanceof Room) {
            Room location = (Room) player.getLocation();
            list.add(location);
            list.addAll(location.getObjects());
            list.addAll(location.getConnectedRooms());
        }

        list.add(player);

        return objectParse(arg, ShardObject.class, list);
    }
}