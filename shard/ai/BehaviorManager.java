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

    /**
     * Gets true or false, 50/50 chance.
     * @return True 50% of the time.
     */
    private static boolean fiftyFifty() { return Math.random() < 0.5; }


    /**
     * Choose the guests action.
     */
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

    /** 
     * Try to kill someone, if holding a weapon. If unable, use a guestAction().
     */
    public static Event murdererAction(Guest murderer) {

        ArrayList<ShardObject> victims =
            Command.KILL.buildCandidateList(murderer);

        ArrayList<ShardObject> inventory =
            Command.INVENTORY.buildCandidateList(murderer);

        // if alone in the room with one person
        if (!inRoomWithPlayer(murderer) && victims.size() == 1) {

            Guest victim = (Guest) victims.get(0);

            // if holding a weapon
            for (ShardObject object : inventory) {
                Item item = (Item) object;
                if (item.getWeapon()) {
                    return new Event(Command.KILL, item, victim);
                }
            }
        }

        Random rand = new Random();

        // is there something to interact with?
        ArrayList<ShardObject> candidates =
            Command.USE.buildCandidateList(murderer);

        if (candidates.size() > 0 && fiftyFifty() == true) {
            int randomIndex = rand.nextInt(candidates.size());
            ShardObject entry = candidates.get(randomIndex);
            return new Event(Command.USE, murderer, entry);
        }

        // is there something movable in the room?
        candidates = Command.TAKE.buildCandidateList(murderer);

        if (candidates.size() > 0 && fiftyFifty() == true) {
            int randomIndex = rand.nextInt(candidates.size());
            ShardObject entry = candidates.get(randomIndex);
            return new Event(Command.TAKE, murderer, entry);
        }

        // is there something movable in the room that isnt a weapon
        candidates = Command.DROP.buildCandidateList(murderer);

        if (candidates.size() > 0 && fiftyFifty() == true) {
            int randomIndex = rand.nextInt(candidates.size());
            Item entry = (Item) candidates.get(randomIndex);

            if (!entry.getWeapon()) {
                return new Event(Command.DROP, murderer, entry);
            }
        }

        // can I go to another room?
        candidates = Command.ENTER.buildCandidateList(murderer);

        if (candidates.size() > 0 && fiftyFifty() == true) {
            int randomIndex = rand.nextInt(candidates.size());
            ShardObject entry = candidates.get(randomIndex);
            return new Event(Command.ENTER, murderer, entry);
        }

        // do nothing
        return new Event(Command.ERROR, murderer, new ShardObject("null"));
    }

    /** Is this guest in a room with the Player? */
    private static boolean inRoomWithPlayer(Guest g) {

        ArrayList<ShardObject> objects = g.getLocation().getObjects();

        for (ShardObject o : objects) {
            if (o instanceof Player) { return true; }
        }

        return false;
    }

    /** Is this guest by himself in his location? */
    private static boolean alone(Guest g) {

        ArrayList<ShardObject> objects = g.getLocation().getObjects();

        for (ShardObject o : objects) {
            if (o instanceof Player || o instanceof Guest) { return false; }
        }

        return true;

    }


}