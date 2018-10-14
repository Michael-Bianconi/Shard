package shard.user;
import shard.object.*;
import java.util.ArrayList;
/**
 * @author Michael Bianconi
 * @author https://www.github.com/Michael-Bianconi
 * @version 2
 *
 * List of different Commands that the player can use.
 */
public enum Command {
    ENTER {
        @Override
        public ArrayList<ShardObject> buildCandidateList(Player player) {

            ArrayList<ShardObject> list = new ArrayList<ShardObject>();
            Room location = (Room) player.getLocation();
            list.addAll(location.getConnectedRooms());
            return filter(Room.class, list);
        }

        @Override
        public int getNumArguments() { return 1; }

        @Override
        public void execute(ShardObject executor, ShardObject location) {

            executor.setLocation((Owner) location);
        }
    },

    DESCRIBE {
        @Override
        public ArrayList<ShardObject> buildCandidateList(Player player) {

            ArrayList<ShardObject> list = new ArrayList<ShardObject>();
            Room location = (Room) player.getLocation();
            list.add(player);
            list.add(location);
            list.addAll(player.getObjects());
            list.addAll(location.getObjects());
            list.addAll(location.getConnectedRooms());

            return list;
        }

        @Override
        public int getNumArguments() { return 1; }

        @Override
        public void execute(ShardObject executor, ShardObject item) {
            System.out.println(item.getDescription());
        }

    },  

    TAKE {
        @Override
        public ArrayList<ShardObject> buildCandidateList(Player player) {

            ArrayList<ShardObject> list = player.getLocation().getObjects();
            return filter(Item.class, list);
        }

        @Override
        public int getNumArguments() { return 1; }

        @Override
        public void execute(ShardObject executor, ShardObject item) {
            item.setLocation((Owner) executor);
        }
    },

    DROP {
        @Override
        public ArrayList<ShardObject> buildCandidateList(Player player) {

            return filter(Item.class, player.getObjects());
        }

        @Override
        public int getNumArguments() { return 1; }

        @Override
        public void execute(ShardObject executor, ShardObject item) {
            item.setLocation(executor.getLocation());
        }

    },

    TALK {
        @Override
        public ArrayList<ShardObject> buildCandidateList(Player player) {

            ArrayList<ShardObject> list = new ArrayList<ShardObject>();
            list.addAll(player.getLocation().getObjects());
            return filter(Guest.class, list);
        }

        @Override
        public int getNumArguments() { return 1; }
    },

    INVESTIGATE {
        @Override
        public void execute(ShardObject executor, ShardObject item) {

            ArrayList<ShardObject> objs = executor.getLocation().getObjects();
            for (ShardObject o : objs) {
                System.out.println(o.getName());
            }
        }

    }, 

    WHOAMI {
        @Override
        public void execute(ShardObject executor, ShardObject item) {
            System.out.println(executor.getName());
        }
    },

    WHEREAMI {

        @Override
        public void execute(ShardObject executor, ShardObject item) {
            ShardObject o = (ShardObject) executor.getLocation();
            System.out.println(o.getName());
        }
    },

    INVENTORY {

        @Override
        public void execute(ShardObject executor, ShardObject item) {

            Owner owner = (Owner) executor;

            for (ShardObject o : owner.getObjects()) {
                System.out.println(o.getName());
            }
        }
    },

    ERROR;

    /**
     * Given a player, create an ArrayList of all possible candidates that
     * could be used with this command. Overridden by individual enums.
     * If not overridden, returns an empty list.
     * @param player Player to search through.
     * @return Return all valid candidates.
     */
    public ArrayList<ShardObject>buildCandidateList(Player player) {
        return new ArrayList<ShardObject>();
    }


    /**
     * Return the number of arguments this command takes. Overridden by
     * individual enums. By default, 0.
     * @return Returns the number of arguments this command takes.
     */
    public int getNumArguments() { return 0; }


    /**
     * Given an executor and an argument, execute the command. If not
     * overridden, do nothing.
     * @param executor ShardObject that is initiating this event.
     * @param item Argument to execute the command with.
     */
    public void execute(ShardObject executor, ShardObject item) {    }


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
}