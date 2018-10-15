package shard.user;

/**
 * The OutputManager formats Strings according to their purpose before
 * printing them.
 *
 * @author Michael Bianconi
 * @author https://www.github.com/Michael-Bianconi
 * @version 1
 * @since 14 October 2018
 */
public class OutputManager {

    private OutputManager() {   }

    public static void print(OutputType type, String string) {
        switch(type) {

            case USER_EVENT:
                System.out.println("> " + string);
                break;

            case CONVERSATION_QUESTION:
                System.out.println("... " + string);
                break;

            case CONVERSATION_RESPONSE:
                System.out.println(">>> " + string);
                break;

            case GUEST_EVENT:
                System.out.println("* " + string);
                break;

            case DESCRIPTION:
                System.out.println("--- " + string);
                break;

            case LIST_ITEM:
                System.out.println("[] " + string);
                break;

            default:
                System.out.println(string);
        }
    }
}