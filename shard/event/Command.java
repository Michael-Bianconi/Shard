package shard.event;
import shard.object.*;
import shard.user.*;
import java.util.ArrayList;
import java.util.Random;
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
        public ArrayList<ShardObject> buildCandidateList(ShardObject target) {

            ArrayList<ShardObject> list = new ArrayList<ShardObject>();
            Room location = (Room) target.getLocation();
            list.addAll(location.getConnectedRooms());
            return filter(Room.class, list);
        }

        @Override
        public int getNumArguments() { return 1; }

        @Override
        public void execute(ShardObject executor, ShardObject location) {

            executor.setLocation((Owner) location);
        }

        @Override
        public boolean remember() { return true; }

        @Override
        public String pastTense() { return "entered"; }
    },

    DESCRIBE {
        @Override
        public ArrayList<ShardObject> buildCandidateList(ShardObject target) {

            ArrayList<ShardObject> list = new ArrayList<ShardObject>();
            Room location = (Room) target.getLocation();
            list.add(target);
            list.add(location);

            Owner o = (Owner) target;

            list.addAll(o.getObjects());
            list.addAll(location.getObjects());
            list.addAll(location.getConnectedRooms());

            return list;
        }

        @Override
        public int getNumArguments() { return 1; }

        @Override
        public void execute(ShardObject executor, ShardObject item) {
            System.out.println(
                OutputManager.format(OutputType.DESCRIPTION,
                                     item.getDescription())
            );
        }

        @Override
        public String pastTense() { return "described"; }

    },  

    TAKE {
        @Override
        public ArrayList<ShardObject> buildCandidateList(ShardObject target) {

            ArrayList<ShardObject> items = new ArrayList<ShardObject>();
        
            for (ShardObject i : filter(Item.class, target.getLocation().getObjects())) {
                Item item = (Item) i;
                if (item.getMovable()) { items.add(i); }
            }

            return items;
        }

        @Override
        public int getNumArguments() { return 1; }

        @Override
        public void execute(ShardObject executor, ShardObject item) {
            Item i = (Item) item;
            if (i.getMovable()) { item.setLocation((Owner) executor); }
        }

        @Override
        public boolean remember() { return true; }

        @Override
        public String pastTense() { return "took"; }
    },

    DROP {
        @Override
        public ArrayList<ShardObject> buildCandidateList(ShardObject target) {

            Owner o = (Owner) target;
            ArrayList<ShardObject> items = new ArrayList<ShardObject>();
        
            for (ShardObject i : filter(Item.class, o.getObjects())) {
                Item item = (Item) i;
                if (item.getMovable()) { items.add(i); }
            }

            return items;
        }

        @Override
        public int getNumArguments() { return 1; }

        @Override
        public void execute(ShardObject executor, ShardObject item) {
            Item i = (Item) item;
            if (i.getMovable()) { item.setLocation(executor.getLocation()); }
            else { System.out.println("immovable object."); }
        }

        @Override
        public boolean remember() { return true; }

        @Override
        public String pastTense() { return "dropped"; }

    },

    USE {
        @Override
        public ArrayList<ShardObject> buildCandidateList(ShardObject target) {
            ArrayList<ShardObject> list = new ArrayList<ShardObject>();

            for (ShardObject i : filter(Item.class, target.getLocation().getObjects())){
                Item item = (Item) i;
                if (item.getUsable()) { list.add(i); }
            }

            Owner o = (Owner) target;
            for (ShardObject i : filter(Item.class, o.getObjects())) {
                Item item = (Item) i;
                if (item.getUsable()) { list.add(i); }
            }

            return list;
        }

        @Override
        public int getNumArguments() { return 1; }

        @Override
        public void execute(ShardObject executor, ShardObject item) {
            Item i = (Item) item;
            Event e = i.use(executor);
            e.execute();
        }

        @Override
        public boolean remember() { return true; }

        @Override
        public String pastTense() { return "used"; }

    },

    KILL {
        @Override
        public ArrayList<ShardObject> buildCandidateList(ShardObject target) {
            ArrayList<ShardObject> list = Command.TALK.buildCandidateList(target);
            list.remove(target);
            return list;
        }

        @Override
        public int getNumArguments() { return 1; }

        // put the guest in his own little room and replace them with a body
        @Override
        public void execute(ShardObject executor, ShardObject item) {

            Guest guest = (Guest) item;
            guest.setDead(true);
            Item deadGuest = new Item(guest.getName(), "he's dead, jim", item.getLocation());
            guest.setLocation(new Room());
        }


    },

    TALK {
        @Override
        public ArrayList<ShardObject> buildCandidateList(ShardObject target) {

            ArrayList<ShardObject> list = new ArrayList<ShardObject>();
            list.addAll(target.getLocation().getObjects());
            return filter(Guest.class, list);
        }

        @Override
        public int getNumArguments() { return 1; }

        @Override
        public void execute(ShardObject executor, ShardObject item) {
            Memory guest = (Memory) item;
            Conversation.converse(guest);
        }

        @Override
        public String pastTense() { return "talked to"; }
    },

    TALK_TO_PLAYER {
        @Override
        public ArrayList<ShardObject> buildCandidateList(ShardObject target) {
            ArrayList<ShardObject> list = new ArrayList<ShardObject>();
            list.addAll(target.getLocation().getObjects());
            return filter(Player.class, list);
        }

        @Override
        public void execute(ShardObject executor, ShardObject item) {
            Player p = (Player) item;
            // stuff
        }

        @Override
        public String pastTense() { return "talked to"; }
    },

    INVESTIGATE {
        @Override
        public void execute(ShardObject executor, ShardObject item) {

            ArrayList<ShardObject> objs = executor.getLocation().getObjects();
            for (ShardObject o : objs) {
                System.out.println(
                    OutputManager.format(OutputType.LIST_ITEM,
                                         o.getName())
                );
            }
        }

    }, 

    WHOAMI {
        @Override
        public void execute(ShardObject executor, ShardObject item) {

            System.out.println(
                OutputManager.format(OutputType.DESCRIPTION,
                                     executor.getName())
            );
        }
    },

    WHEREAMI {

        @Override
        public void execute(ShardObject executor, ShardObject item) {

            ShardObject o = (ShardObject) executor.getLocation();
            System.out.println(
                OutputManager.format(OutputType.DESCRIPTION,
                                     o.getName())
            );
        }
    },

    INVENTORY {

        @Override
        public void execute(ShardObject executor, ShardObject item) {

            Owner owner = (Owner) executor;

            for (ShardObject o : owner.getObjects()) {
                System.out.println(
                    OutputManager.format(OutputType.LIST_ITEM,
                                         o.getName())
                );
            }
        }
    },

    QUIT {
        @Override
        public void execute(ShardObject executor, ShardObject item) {
            System.exit(0);
        }
    },

    ERROR;

    /**
     * Given a player, create an ArrayList of all possible candidates that
     * could be used with this command. Overridden by individual enums.
     * If not overridden, returns an empty list.
     * @param target Player to search through.
     * @return Return all valid candidates.
     */
    public ArrayList<ShardObject>buildCandidateList(ShardObject target) {
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
    public void execute(ShardObject executor, ShardObject item) {
        //System.out.println("Executing default");
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
     * Boolean flag for if guests should remember these events happening.
     * By default, false.
     */
    public boolean remember() { return false; }


    /**
     * The past tense of the command. By default, the lowercase name.
     */
    public String pastTense() { return name().toLowerCase(); }
}