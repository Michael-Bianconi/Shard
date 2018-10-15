package shard.user;
import shard.object.Player;

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

    public static String format(OutputType type, String string) {
        return type.prefix() + string + type.suffix();
    }

    public static String format(Event e) {

        String formattedString = "";

        if (e.getExecutor() instanceof Player) {

            formattedString =
                OutputType.USER_EVENT.prefix() +
                "You " + e.getCommand().pastTense();
        }

        else {
            formattedString =
                OutputType.GUEST_EVENT.prefix() +
                e.getExecutor().getName() + " " + e.getCommand().pastTense();
        }

        if (e.getArgument() != null) {
            formattedString += " " + e.getArgument().getName();
        }

        return formattedString;
    }
}