package shard.ai;
import shard.object.*;
import shard.event.*;
import java.util.Random;
import java.util.ArrayList;

/**
 * Handle AI for Guests.
 *
 * @author Michael Bianconi
 * @author https://www.github.com/Michael-Bianconi
 * @version 2
 * @since 19 October 2018
 */
public class BehaviorManager {

    private BehaviorManager() {   }

    private static boolean fiftyFifty() { return Math.random() < 0.5; }

    public static Event action(Guest g) {

        Random rand = new Random();

        // is there something to interact with?
        ArrayList<ShardObject> candidates =
            Command.USE.buildCandidateList(g);

        if (candidates.size() > 0 && fiftyFifty() == true) {
            int randomIndex = rand.nextInt(candidates.size());
            ShardObject entry = candidates.get(randomIndex);
            return new Event(Command.USE, g, entry);
        }

        // is there something movable in the room?
        candidates = Command.TAKE.buildCandidateList(g);

        if (candidates.size() > 0 && fiftyFifty() == true) {
            int randomIndex = rand.nextInt(candidates.size());
            ShardObject entry = candidates.get(randomIndex);
            return new Event(Command.TAKE, g, entry);
        }

        // is there something movable in the room?
        candidates = Command.DROP.buildCandidateList(g);

        if (candidates.size() > 0 && fiftyFifty() == true) {
            int randomIndex = rand.nextInt(candidates.size());
            ShardObject entry = candidates.get(randomIndex);
            return new Event(Command.DROP, g, entry);
        }

        // can I go to another room?
        candidates = Command.ENTER.buildCandidateList(g);

        if (candidates.size() > 0 && fiftyFifty() == true) {
            int randomIndex = rand.nextInt(candidates.size());
            ShardObject entry = candidates.get(randomIndex);
            return new Event(Command.ENTER, g, entry);
        }

        // do nothing
        return new Event(Command.ERROR, g, new ShardObject("null"));
    }


}