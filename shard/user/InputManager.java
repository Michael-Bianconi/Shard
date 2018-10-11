/**
 * Shard
 * @author Michael Bianconi
 */
package shard.user;
import shard.event.Event;
import shard.event.EventCode;
import shard.object.Player;
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
     */
}