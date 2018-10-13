package shard.user;
import shard.event.Event;
import shard.event.EventCode;
import shard.object.Player;
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

    private static final Map<String, EventCode> codeMap;
    static {
        codeMap = new HashMap<String, EventCode>();

        codeMap.put("ENTER",       EventCode.ENTER);
        codeMap.put("GOTO",        EventCode.ENTER);
        codeMap.put("GO TO",       EventCode.ENTER);

        codeMap.put("DESCRIBE",    EventCode.DESCRIBE);
        codeMap.put("HELP",        EventCode.DESCRIBE);
        codeMap.put("INFORMATION", EventCode.DESCRIBE);
        codeMap.put("INFO",        EventCode.DESCRIBE);

        codeMap.put("TAKE",        EventCode.TAKE);
        codeMap.put("PICKUP",      EventCode.TAKE);
        codeMap.put("PICK UP",     EventCode.TAKE);

        codeMap.put("DROP",        EventCode.DROP);
        codeMap.put("LEAVE",       EventCode.DROP);

        codeMap.put("INVESTIGATE", EventCode.INVESTIGATE);
        codeMap.put("LOOK AROUND", EventCode.INVESTIGATE);

        codeMap.put("TALK",        EventCode.TALK);
        codeMap.put("TALK TO",     EventCode.TALK);
        codeMap.put("TALK WITH",   EventCode.TALK);
    }

    /**
     * This method takes user input (e.g. "describe lounge"),
     * and turns it into an Event (e.g. DESCRIBE PLAYER LOUNGE).
     * Only ShardObjects relevant to the player are considered.
     *
     * @param p Player object.
     * @param input String input.
     * @return Shard-readable Event.
     * @exception InvalidInputException
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