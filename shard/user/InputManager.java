package shard.user;
import shard.object.*;
import shard.event.*;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
import java.util.ArrayList;

/**
 * Handles user input.
 *
 * @author Michael Bianconi
 * @author https://www.github.com/Michael-Bianconi
 * @version 2
 */
public class InputManager {

    // used by candidateCompare to signal that the argument and
    // the candidate names are an exact match.
    private static final int EXACT_MATCH = 999;

    // used by candidateCompare to signal that the argument either shared
    // no keywords, or had keywords not included in the candidate name
    private static final int NO_MATCH = 0;

    /** All possible commands, mapped to the corresponding Commands. */
    private static final Map<String, Command> commandMap;

    /** When comparing keywords, if these keywords are used in the argument
        but don't match any keywords in the candidate, ignore them it's fine.
    */
    private static final ArrayList<String> ignoredKeywords;

    static {
        commandMap = new HashMap<String, Command>();
        commandMap.put("ENTER",         Command.ENTER);
        commandMap.put("GOTO",          Command.ENTER);
        commandMap.put("GO TO",         Command.ENTER);
        commandMap.put("DESCRIBE",      Command.DESCRIBE);
        commandMap.put("HELP",          Command.DESCRIBE);
        commandMap.put("INFORMATION",   Command.DESCRIBE);
        commandMap.put("INFO",          Command.DESCRIBE);
        commandMap.put("TAKE",          Command.TAKE);
        commandMap.put("PICKUP",        Command.TAKE);
        commandMap.put("PICK UP",       Command.TAKE);
        commandMap.put("DROP",          Command.DROP);
        commandMap.put("LEAVE",         Command.DROP);
        commandMap.put("USE",           Command.USE);
        commandMap.put("INTERACT",      Command.USE);
        commandMap.put("INTERACT WITH", Command.USE);
        commandMap.put("INVESTIGATE",   Command.INVESTIGATE);
        commandMap.put("LOOK AROUND",   Command.INVESTIGATE);
        commandMap.put("TALK",          Command.TALK);
        commandMap.put("TALK TO",       Command.TALK);
        commandMap.put("TALK WITH",     Command.TALK);
        commandMap.put("WHOAMI",        Command.WHOAMI);
        commandMap.put("WHOAMI?",       Command.WHOAMI);
        commandMap.put("WHO AM I",      Command.WHOAMI);
        commandMap.put("WHO AM I?",     Command.WHOAMI);
        commandMap.put("WHEREAMI",      Command.WHEREAMI);
        commandMap.put("WHEREAMI?",     Command.WHEREAMI);
        commandMap.put("WHERE AM I",    Command.WHEREAMI);
        commandMap.put("WHERE AM I?",   Command.WHEREAMI);
        commandMap.put("INVENTORY",     Command.INVENTORY);
        commandMap.put("QUIT",          Command.QUIT);
        commandMap.put("SKIP",          Command.SKIP);

        ignoredKeywords = new ArrayList<String>();
        ignoredKeywords.add("THE");
        ignoredKeywords.add("THAT");
        ignoredKeywords.add("THIS");
        ignoredKeywords.add("TO");
        ignoredKeywords.add("A");
    }

    private InputManager() {}

    /**
     * Parses user input into an Event. If the input is unrecognized or
     * ambiguous, throws an InvalidInputException.
     * @param player Player object.
     * @param input String input.
     * @return Corresponding Event.
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

        Command command = Command.valueOf(input.substring(0, commandIndex));

        // dealing with 0-arg inputs
        if (commandIndex != input.length()) {
            input = input.substring(commandIndex+1, input.length());
        }

        else {
            input = "";
        }

        // check for 0-arg commands
        if (command.getNumArguments() == 0) {
            if (input.length() == 0) {
                return new Event(command, player);
            }

            // got arguments in a 0-arg command
            else {
                throw new InvalidInputException("Unexpected arguments.");
            }
        }

        // expected args and didn't get any
        else if (input.length() == 0) {
            throw new InvalidInputException("Missing arguments!");
        }

        else {
            candidateList = command.buildCandidateList(player);
            ShardObject object = matchArgument(input, candidateList);
            return new Event(command, player, object);
        }
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
        Command code = null;

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
     * Given a String argument and a list of ShardObject candidates,
     * find the one that most closely matches the argument. If two
     * candidates match equally, consider the input ambiguous and
     * throw an InvalidInputException.
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
            throw new InvalidInputException("Cannot : " + arg);
        }

        else if (ambiguous) {
            throw new InvalidInputException("Ambiguous argument: " + arg);
        }

        else {
            return matchObject;
        }


    }


    /**
     * Determine how closely two String match each other by comparing
     * how many keywords they share.
     * @param arg The argument
     * @param candidate The candidate's name
     * @return Returns EXACT_MATCH if arg.equals(candidate). 
     *         Returns NO_MATCH if arg has a keyword that candidate does not. 
     *         Otherwise returns the number of shared keywords.
     */
    private static int compareKeywords(String arg, String candidate) {

        String argument = arg.toUpperCase();
        candidate = candidate.toUpperCase();

        if (argument.equals(candidate)) {
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