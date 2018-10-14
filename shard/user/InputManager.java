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
import java.util.Arrays;
import java.util.ArrayList;

/**
 * Shard
 * @author Michael Bianconi
 * @author https://www.github.com/Michael-Bianconi
 * @version 2
 *
 * The InputManager takes input and turns into Shard-readable Events.
 *
 *
 * Update 14 October 2018
 * 
 * I'm very pleased with the InputManger. The file itself is pretty long
 * and complex, but it works well. It can parse several several commands,
 * and any variations of those commands if they're specified in the
 * commandMap variable.
 *
 * Mostly, however, I'm pleased that this manager can guess at what the player
 * wants, so long there isn't any ambiguity.
 *
 * However, it's not a perfect system, and it has limitations.
 *
 * Namely, it can only handle single argument commands where the command
 * keyword precedes the argument. It can also only handle arguments that match
 * to objects of a single type (although that type can be ShardObject).
 *
 * Secondly, developers have to be careful that they correctly implement
 * what each Event requires. That includes:
 *      adding the eventcode name and variations to the commandMap.
 *      adding the argument type to filterMap
 *      adding the number of arguemnts to the argCountMap (must be 0 or 1)
 *      adding the eventcode to buildCandidateList()
 *
 * It's not terrible, but it is kind of a pain and things'll go tits up if
 * one of those steps are forgotten.
 *
 * Finally, the parser can choose to ignore some words to make sense of input.
 * These words, like "the" or "that", can then be placed in weird spots and
 * the input will still be parsed. This is usually great, but it means
 * input like "talk to the fred the barnes the" will still be interpreted
 * as "TALK FRED BARNES".
 *
 * Here's how the InputManager works.
 *
 * The only public method is parse(player, input).
 *
 * 1. The method takes the input, reads in and translates the command into
 *    something Shard-readable (e.g. "go to room" becomes "ENTER room"). If
 *    the command is not recognized, an InvalidInputException is thrown.
 *
 * 2. It determines how many arguments are needed for the command and makes sure
 *    it was supplied with the correct number. If it is a 0-argument
 *    command, it returns the appropriate event. If the wrong number of
 *    arguments are supplied for the command, an InvalidInputException is
 *    thrown.
 *
 * 3. It builds a list of candidates appropriate for the command (e.g. TALK
 *    with build a list of all the Guests in the current room).
 *
 * 4. It compares the given argument to each object's name in the list.
 *    If it comes across an exact match, it immediately selects that candidate.
 *    Otherwise, it counts how many keywords are shared between the argument
 *    and the candidate (e.g. "fred" and "fred barnes" = 1).
 *
 * 5. As it compares each object, it looks to see if that object is the best
 *    match by comparing the number of shared keywords. After going through
 *    each candidate, it returns the closest one. If two candidates have the
 *    closeness rating, the input is declared ambiguous and an
 *    InvalidInputException is thrown.
 */
public class InputManager {

    // used by candidateCompare to signal that the argument and
    // the candidate names are an exact match.
    private static final int EXACT_MATCH = 999;

    // used by candidateCompare to signal that the argument either shared
    // no keywords, or had keywords not included in the candidate name
    private static final int NO_MATCH = 0;

    /** All possible commands, mapped to the corresponding EventCodes. */
    private static final Map<String, EventCode> commandMap;

    /** Which kind of ShardObject does an EventCode care about? */
    private static final Map<EventCode, Class> filterMap;

    /** How many arguments can be used with a command? */
    private static final Map<EventCode, Integer> argCountMap;

    /** When comparing keywords, if these keywords are used in the argument
        but don't match any keywords in the candidate, ignore them it's fine.
    */
    private static final ArrayList<String> ignoredKeywords;

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

        argCountMap = new HashMap<EventCode, Integer>();
        argCountMap.put(EventCode.ENTER,          1);
        argCountMap.put(EventCode.DESCRIBE,       1);
        argCountMap.put(EventCode.TAKE,           1);
        argCountMap.put(EventCode.DROP,           1);
        argCountMap.put(EventCode.INVESTIGATE,    0);
        argCountMap.put(EventCode.TALK,           1);
        argCountMap.put(EventCode.WHOAMI,         0);
        argCountMap.put(EventCode.WHEREAMI,       0);

        ignoredKeywords = new ArrayList<String>();
        ignoredKeywords.add("THE");
        ignoredKeywords.add("THAT");
        ignoredKeywords.add("THIS");
        ignoredKeywords.add("TO");
    }

    private InputManager() {}

    /**
     * Parse input. This is InputManager's only public method. See
     * class documentation for more information on this method.
     * @param p Player object.
     * @param input String input.
     * @return Shard-readable Event.
     * @exception InvalidInputException
     */
    public static Event parse(Player player, String input)
        throws InvalidInputException {

        // input arguments are matched with something in this list.
        ArrayList<ShardObject> candidateList = new ArrayList<ShardObject>();

        // Get a Shard-readable command
        input = translateCommand(input);

        // remove and store the command string
        int commandIndex = input.indexOf(' ');
        if (commandIndex == -1) { commandIndex = input.length(); }

        EventCode command = EventCode.valueOf(input.substring(0, commandIndex));

        // dealing with 0-arg inputs
        if (commandIndex != input.length()) {
            input = input.substring(commandIndex+1, input.length());
        }

        else {
            input = "";
        }

        // check for 0-arg commands
        if (argCountMap.containsKey(command) && argCountMap.get(command) == 0) {
            if (input.length() == 0) {
                return new Event(command);
            }

            else {
                throw new InvalidInputException("Unexpected arguments.");
            }
        }

        // error check for no arguments
        if (argCountMap.containsKey(command) && argCountMap.get(command) == 1) {
            if (input.length() == 0) {
                throw new InvalidInputException("Missing arguments!");
            }

            // build the candidate list
            // match the argument
            // return the new event
            else {
                candidateList = buildCandidateList(command, player);
                ShardObject object = matchArgument(input, candidateList);
                return new Event(command, object);
            }
        }

        throw new InvalidInputException("Bad command!");
    }


    /**
     * Given the input, try to determine the command. If it gets found,
     * return a new input string with the Shard-readable command.
     * For example, "talk to fred barnes" becomes "TALK fred barnes".
     * This method will return the first, longest code match. For example,
     * "talk talk to fred barnes" with return "TALK talk to fred barnes".
     * @param input String to parse.
     * @return New String with a Shard-readable command.
     * @exception InvalidInputException if the command is invalid.
     * @see parse()
     */
    private static String translateCommand(String input)
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


    /**
     * Build a list of candidates given a command.
     * @param command Command to build candidates for.
     * @param player Player to get candidates from.
     * @return A list of all possible candidates.
     * @exception InvalidInputException
     * @see filter()
     */
    private static ArrayList<ShardObject> buildCandidateList(
        EventCode command, Player player)
        throws InvalidInputException {

        ArrayList<ShardObject> list;
        Room location = (Room) player.getLocation();

        switch(command) {

            // make a list of all connected rooms
            case ENTER:
                list = new ArrayList<ShardObject>();
                list.addAll(location.getConnectedRooms());
                return filter(filterMap.get(command), list);

            // make a list of everything
            case DESCRIBE:
                list = new ArrayList<ShardObject>();
                list.add(player);
                list.add(location);
                list.addAll(player.getObjects());
                list.addAll(location.getObjects());
                list.addAll(location.getConnectedRooms());

                return list;

            // make a list of items in the room
            case TAKE:
                list = player.getLocation().getObjects();
                return filter(filterMap.get(command), list);

            // make a list of items held by the player
            case DROP:
                return filter(filterMap.get(command), player.getObjects());

            // make a list of Guests in the room.
            case TALK:
                list = new ArrayList<ShardObject>();
                list.addAll(player.getLocation().getObjects());
                return filter(filterMap.get(command), list);

            // This shouldn't happen, and it means I forgot to add
            // an event code to the switch statement
            default:
                throw new InvalidInputException("Forgot to implement " +
                                                command.name());
        }
    }


    /**
     * Given a class and a list of Objects, return a list with only entries with
     * the same class or superclass.
     * @param filterClass Class to filter to.
     * @param list List of ShardObjects to filter.
     * @return A new list.
     */
    private static ArrayList<ShardObject> filter(Class filterClass,
                                                 ArrayList<ShardObject> list) {
        ArrayList<ShardObject> filteredList = new ArrayList<ShardObject>();

        for (ShardObject o : list) {
            if (filterClass.isInstance(o)) {
                filteredList.add(o);
            }
        }

        return filteredList;
    }


    /**
     * Match the given argument to some object in the given list.
     * When matching arguments, use the getName() method of the
     * ShardObject.
     *
     * There may be some ambiguity when trying to match candidates.
     * For example, "FRED" will match to "FRED BARNES" and "FRED FREDRICKSON".
     * In this case, an InvalidInputException is thrown with the the String
     * "Multiple " + arg + " exist!".
     *
     * @param arg Argument to match.
     * @param list List of possible candidates.
     * @return The matched candidate.
     * @exception InvalidInputException if no match is found.
     */
    private static ShardObject matchArgument(String arg,
                                             ArrayList<ShardObject> list)
                                             throws InvalidInputException {
        ShardObject matchObject = null;
        int matchRating = 0;
        boolean ambiguous = false;

        for (ShardObject o : list) {

            int sharedKeywords = compareKeywords(arg, o.getName());

            // if there's a perfect match, just return it. If this is
            // ambiguous, that's the fault of the developer (me) for giving
            // two objects the same name
            if (sharedKeywords == InputManager.EXACT_MATCH) {
                return o;
            }

            // can't tell which object the player wanted
            else if (sharedKeywords == matchRating) {
                ambiguous = true;
            }

            // there is a closer match
            else if (sharedKeywords > matchRating) {
                ambiguous = false;
                matchRating = sharedKeywords;
                matchObject = o;
            }
        }

        if (matchObject == null) {
            throw new InvalidInputException("Cannot find: " + arg);
        }

        else if (ambiguous) {
            throw new InvalidInputException("Ambiguous argument: " + arg);
        }

        else {
            return matchObject;
        }


    }


    /**
     * Given two Strings, determine how closely they match to each other.
     * Closeness is determined by how many words they share. If both share
     * all the same words, in the same order, they are an exact match.
     *
     * e.g.
     *      FRED BARNES,    FRED BARNES = EXACT_MATCH
     *      FRED,           FRED BARNES = 1
     *      BARNES,         FRED BARNES = 1
     *      LUKE,           FRED BARNES = NO_MATCH
     *
     *      BLOODY FRYING PAN,  BLOODY FRYING PAN = EXACT_MATCH
     *      BLOODY FRYING,      BLOODY FRYING PAN = 2
     *      PAN,                BLOODY FRYING PAN = 1
     *      
     *
     * @param arg The argument
     * @param candidate The candidate's name
     * @return Returns EXACT_MATCH if arg.equals(candidate).
     *         Returns NO_MATCH if arg has a keyword that candidate does not
     *         Otherwise returns the number of shared keywords.
     */
    private static int compareKeywords(String arg, String candidate) {

        System.out.println("Comparing: " + arg + " to " + candidate);

        String argument = arg.toUpperCase();
        candidate = candidate.toUpperCase();

        if (argument.equals(candidate)) {
            System.out.println(argument + " exact match");
            return InputManager.EXACT_MATCH;
        }

        // break up the strings into keywords
        ArrayList<String> argumentTokens =
            new ArrayList<String>(Arrays.asList(argument.split(" ")));

        ArrayList<String> candidateTokens =
            new ArrayList<String>(Arrays.asList(candidate.split(" ")));

        int numSharedKeywords = 0;

        // check each keyword. If the argument contains a keyword found in
        // the candidate, then remove that keyword from the candidate and
        // increment the number of shared keywords.
        // If the argument contains a keyword NOT found in the candidate,
        // then NO_MATCH is returned.
        for (String s : argumentTokens) {

            // remove(Object o) returns true if the list contained o
            boolean hadKeyword = candidateTokens.remove(s);

            // the keyword existed in the candidate name
            if (hadKeyword) {
                numSharedKeywords++;
            }

            // there was an argument keyword that was not a part of the name
            // (or ignored)
            else if (!hadKeyword && !ignoredKeywords.contains(s)) {
                return InputManager.NO_MATCH;
            }
        }

        return numSharedKeywords;
    }
}