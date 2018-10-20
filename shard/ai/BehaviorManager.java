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

    public static Event guestAction(Guest g) {

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

    public static Event murdererAction(Guest g) {

        ArrayList<ShardObject> candidates = Command.KILL.buildCandidateList(g);

        // if alone in the room with a person
        if (!inRoomWithPlayer(g) && candidates.size() == 1) {
            return new Event(Command.KILL, g, candidates.get(0));
        }

        else {
            return guestAction(g);
        }

    }

    private static boolean inRoomWithPlayer(Guest g) {

        ArrayList<ShardObject> objects = g.getLocation().getObjects();

        for (ShardObject o : objects) {
            if (o instanceof Player) { return true; }
        }

        return false;
    }


}