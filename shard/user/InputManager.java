/**
 * Shard
 * @author Michael Bianconi
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
        System.out.println(Arrays.toString(arr));
        EventCode c;

        // Read in the event code or return an Error
        try {
            c = EventCode.valueOf(arr[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CommandNotFoundException("Unknown command!");
        }

        ArrayList<ShardObject> objectList;

        // parse the command arguments
        try {
            // args should be limited to Rooms
            if (c == EventCode.GOTO || c == EventCode.INVESTIGATE) {

                Room r = this.player.getLocation();
                objectList = objectParse(arr[1],
                                         Room.class, 
                                         (ArrayList<ShardObject>)(ArrayList<?>) r.getConnectedRooms());
            }

            // args should be limited to Items held by the Player
            else if (c == EventCode.DROP) {
                objectList = objectParse(arr[1],
                                         Item.class,
                                         this.player.getObjects());
            }

            // args should be limited to Items in the Room
            else if (c == EventCode.TAKE) {
                objectList = objectParse(arr[1],
                                         Item.class,
                                         this.player.getLocation().getObjects());
            }

            // args should be limited to People in the room
            else if (c == EventCode.TALK) {
                objectList = objectParse(arr[1],
                                         Person.class,
                                         this.player.getLocation().getObjects());
            }

            // args can be literally anything on the Player or in the room
            else if (c == EventCode.DESCRIBE) {
                ArrayList<ShardObject> all = new ArrayList<ShardObject>();
                all.addAll(this.player.getObjects());
                all.addAll(this.player.getLocation().getObjects());
                objectList = objectParse(arr[1],
                                         ShardObject.class,
                                         all);
            }

            else if (c == EventCode.ERROR) {
                objectList = new ArrayList<ShardObject>();
            }

            else {
                throw new CommandNotFoundException("This should never happen.");
            }

        } catch (ArgumentNotFoundException e) {
            throw e;
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new ArgumentNotFoundException("Argument not found!");
        }

        return new Event(c, objectList);
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
    private static ArrayList<ShardObject> objectParse(String name,
                                                      Class filter,
                                                      ArrayList<ShardObject> objs)
                                          throws ArgumentNotFoundException {

        ArrayList<ShardObject> parsedArgs = new ArrayList<ShardObject>();
        
        for (ShardObject o : objs) {
            if (filter.equals(o.getClass())) {
                if (name.equals(o.toString())) {
                    parsedArgs.add(o);
                    return parsedArgs;
                }
            }
        }

        throw new ArgumentNotFoundException("Can't find " + name);
    }
}