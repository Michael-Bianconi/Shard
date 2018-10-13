package shard.user;
import shard.event.Event;
import shard.event.EventCode;
import shard.object.Player;
import shard.object.ShardObject;
import shard.object.Room;
import shard.object.Item;
import shard.object.Owner;
import shard.object.Guest;
import java.util.Map;
import java.util.HashMap;

/**
 * Shard
 * @author Michael Bianconi
 * @author https://www.github.com/Michael-Bianconi
 *
 * The InputManager takes input and turns into Shard-readable Events.
 * The main difficult is efficiently parsing input.
 */
public class InputManager {

    /** All possible commands, mapped to the corresponding EventCodes. */
    private static final Map<String, EventCode> commandMap;

    /** Which kind of ShardObject does an EventCode care about? */
    private static final Map<EventCode, Class> filterMap;

    /** How many arguments can be used with a command? */
    private static final Map<EventCode, int[]> argCountMap;

    static {
        commandMap = new HashMap<String, EventCode>();
        commandMap.put("ENTER",         EventCode.ENTER);
        commandMap.put("GOTO",          EventCode.ENTER);
        commandMap.put("GO TO",         EventCode.ENTER);
        commandMap.put("DESCRIBE",      EventCode.DESCRIBE);
        commandMap.put("HELP",          EventCode.DESCRIBE);
        commandMap.put("INFORMATION",   EventCode.DESCRIBE);
        commandMap.put("INFO",          EventCode.DESCRIBE);
        commandMap.put("TAKE",          EventCode.TAKE);
        commandMap.put("PICKUP",        EventCode.TAKE);
        commandMap.put("PICK UP",       EventCode.TAKE);
        commandMap.put("DROP",          EventCode.DROP);
        commandMap.put("LEAVE",         EventCode.DROP);
        commandMap.put("INVESTIGATE",   EventCode.INVESTIGATE);
        commandMap.put("LOOK AROUND",   EventCode.INVESTIGATE);
        commandMap.put("TALK",          EventCode.TALK);
        commandMap.put("TALK TO",       EventCode.TALK);
        commandMap.put("TALK WITH",     EventCode.TALK);
        commandMap.put("WHOAMI",        EventCode.WHOAMI);
        commandMap.put("WHOAMI?",       EventCode.WHOAMI);
        commandMap.put("WHO AM I",      EventCode.WHOAMI);
        commandMap.put("WHO AM I?",     EventCode.WHOAMI);
        commandMap.put("WHEREAMI",      EventCode.WHEREAMI);
        commandMap.put("WHEREAMI?",     EventCode.WHEREAMI);
        commandMap.put("WHERE AM I",    EventCode.WHEREAMI);
        commandMap.put("WHERE AM I?",   EventCode.WHEREAMI);

        filterMap = new HashMap<EventCode, Class>();
        filterMap.put(EventCode.ENTER,          Room.class);
        filterMap.put(EventCode.DESCRIBE,       ShardObject.class);
        filterMap.put(EventCode.TAKE,           Item.class);
        filterMap.put(EventCode.DROP,           Item.class);
        filterMap.put(EventCode.INVESTIGATE,    Room.class);
        filterMap.put(EventCode.TALK,           Guest.class);
        filterMap.put(EventCode.WHOAMI,         Player.class);
        filterMap.put(EventCode.WHEREAMI,       Owner.class);

        argCountMap = new HashMap<EventCode, int[]>();
        argCountMap.put(EventCode.ENTER,          new int[] {1});
        argCountMap.put(EventCode.DESCRIBE,       new int[] {1});
        argCountMap.put(EventCode.TAKE,           new int[] {1});
        argCountMap.put(EventCode.DROP,           new int[] {1});
        argCountMap.put(EventCode.INVESTIGATE,    new int[] {0, 1});
        argCountMap.put(EventCode.TALK,           new int[] {1});
        argCountMap.put(EventCode.WHOAMI,         new int[] {0});
        argCountMap.put(EventCode.WHEREAMI,       new int[] {0});
    }

    private InputManager() {}

    /**
     * When parsing input arguments, we have to choose from whichever
     * items are available to the player, no more no less. To do that,
     * we use the various compileCandidateList() methods.
     *
     * @param p Player object.
     * @param input String input.
     * @return Shard-readable Event.
     * @exception InvalidInputException
     * @see parseCommand
     */
    public static String parse(Player p, String input)
        throws InvalidInputException {

        return parseCommand(input);

    }

    /**
     * Given the input, try to determine the command. If it gets found,
     * return a new input string with the Shard-readable command.
     * For example, "talk to fred barnes" becomes "TALK fred barnes".
     * This method will return the first code match. For example,
     * "talk talk to fred barnes" with return "TALK talk to fred barnes".
     *
     * See parse() for possible issues.
     *
     * @param input String to parse.
     * @return New String with a Shard-readable command.
     * @exception InvalidInputException if the command is invalid.
     * @see parse()
     */
    private static String parseCommand(String input)
        throws InvalidInputException {

        String[] tokens = input.split(" ");

        String sub = "";
        int commandEndIndex = 0;
        EventCode code = null;

        // get the longest substring, starting at 0, that matches a command
        for (String t : tokens) {
            sub += t.toUpperCase();
            if (commandMap.containsKey(sub)) {
                code = commandMap.get(sub);
                commandEndIndex = sub.length();
            }
            sub += " ";
        }

        // no such substring was found
        if (code == null) {
            throw new InvalidInputException("Unrecognized command!");
        }

        // return a more Shard-readable version of the input
        // e.g. "go to lounge" becomes "ENTER lounge"
        return code.name() + input.substring(commandEndIndex, input.length());
    }
}