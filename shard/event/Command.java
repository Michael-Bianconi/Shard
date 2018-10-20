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

    /** Enter another room (does not have to be connected). */
    ENTER {

        /** Build a list of connected rooms. */
        @Override
        public ArrayList<ShardObject> buildCandidateList(ShardObject target) {

            ArrayList<ShardObject> list = new ArrayList<ShardObject>();
            Room location = (Room) target.getLocation();
            list.addAll(location.getConnectedRooms());
            return filter(Room.class, list);
        }

        /** Takes one argument. */
        @Override
        public int getNumArguments() { return 1; }

        /**
         * Sets the executor's location to the given location.
         * Location must be an Owner.
         */
        @Override
        public void execute(ShardObject executor, ShardObject location) {

            executor.setLocation((Owner) location);
        }

        /** Guests will remember this. */
        @Override
        public boolean remember() { return true; }

        @Override
        public String pastTense() { return "entered"; }
    },

    /** Print out a description (the executor has no impact on the result). */
    DESCRIBE {

        /** Literally every ShardObject the target has access to. */
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

        /** Takes one argument */
        @Override
        public int getNumArguments() { return 1; }

        /** Print out the description of the given object. */
        @Override
        public void execute(ShardObject executor, ShardObject item) {
            System.out.println(
                OutputManager.format(OutputType.DESCRIPTION,
                                     item.getDescription())
            );
        }

        @Override
        public String pastTense() { return "described"; }

        @Override
        public boolean consumesAction() { return false; }

    },  

    /** Pick up an object from the environment. Must be movable. */
    TAKE {

        /** List of all movable objects in the target's location. */
        @Override
        public ArrayList<ShardObject> buildCandidateList(ShardObject target) {

            ArrayList<ShardObject> items = new ArrayList<ShardObject>();
            ArrayList<ShardObject> inRoom = target.getLocation().getObjects();
        
            for (ShardObject i : filter(Item.class, inRoom)) {
                Item item = (Item) i;
                if (item.getMovable()) { items.add(i); }
            }

            return items;
        }

        /** Takes one argument. */
        @Override
        public int getNumArguments() { return 1; }

        /**
         * Set the item's location to be the executor. The executor must be
         * an Owner.
         */
        @Override
        public void execute(ShardObject executor, ShardObject item) {
   
            item.setLocation((Owner) executor);
        }

        @Override
        public boolean remember() { return true; }

        @Override
        public String pastTense() { return "took"; }
    },

    /** Remove an item from the target's inventory. */
    DROP {

        /** Build a list of movable items in the target's inventory. */
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

        /** The item in the executor's location's inventory. */
        @Override
        public void execute(ShardObject executor, ShardObject item) {
            Item i = (Item) item;
            item.setLocation(executor.getLocation());
        }

        @Override
        public boolean remember() { return true; }

        @Override
        public String pastTense() { return "dropped"; }

    },

    /** Interact with an object. */
    USE {

        /** Build a list of usable objects. */
        @Override
        public ArrayList<ShardObject> buildCandidateList(ShardObject target) {
            ArrayList<ShardObject> list = new ArrayList<ShardObject>();
            ArrayList<ShardObject> inRoom = target.getLocation().getObjects();

            for (ShardObject i : filter(Item.class, inRoom)) {
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

        /** Get the Item's onUse event and execute it. */
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

    /** Kill a guest. */
    KILL {

        /** Build a list of all people in the room excluding the target. */
        @Override
        public ArrayList<ShardObject> buildCandidateList(ShardObject target) {

            ArrayList<ShardObject> list =
                Command.TALK.buildCandidateList(target);

            list.remove(target);
            return list;
        }

        @Override
        public int getNumArguments() { return 1; }

        // put the guest in his own little room and replace them with a body
        @Override
        public void execute(ShardObject executor, ShardObject item) {

            Guest guest = (Guest) item;
            Owner location = item.getLocation();
            String description = "Here lies " + guest.getName() + ", dead.";


            guest.setDead(true);
            Item deadGuest = new Item(guest.getName(), description, location);
            guest.setLocation(new Room());
        }

        @Override
        public boolean remember() { return true; }

        @Override
        public String pastTense() { return "killed"; }
    },

    /** Talk to a guest. Player only. */
    TALK {

        /** Build a list of Guests in the target's room. */
        @Override
        public ArrayList<ShardObject> buildCandidateList(ShardObject target) {

            ArrayList<ShardObject> list = new ArrayList<ShardObject>();
            list.addAll(target.getLocation().getObjects());
            return filter(Guest.class, list);
        }

        @Override
        public int getNumArguments() { return 1; }

        /** Have a Conversation with the guest. */
        @Override
        public void execute(ShardObject executor, ShardObject item) {
            Memory guest = (Memory) item;
            Conversation.converse(guest);
        }

        @Override
        public String pastTense() { return "talked to"; }
    },

    /** Print out the Items in the room. */
    INVESTIGATE {

        /** Print out all the items in the room. */
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

    /** Print out who this is. */
    WHOAMI {

        /** Print the name of the executor. */
        @Override
        public void execute(ShardObject executor, ShardObject item) {

            System.out.println(
                OutputManager.format(OutputType.DESCRIPTION,
                                     executor.getName())
            );
        }

        @Override
        public boolean consumesAction() { return false; }
    },

    /** Print out where this is. */
    WHEREAMI {

        /** Print out this executor's location. */
        @Override
        public void execute(ShardObject executor, ShardObject item) {

            ShardObject o = (ShardObject) executor.getLocation();
            System.out.println(
                OutputManager.format(OutputType.DESCRIPTION,
                                     o.getName())
            );
        }

        @Override
        public boolean consumesAction() { return false; }
    },

    /** Print out the objects held by this person. */
    INVENTORY {

        /** Print out the executor's inventory. */
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

        @Override
        public boolean consumesAction() { return false; }
    },

    /** Quit the game. */
    QUIT {
        @Override
        public void execute(ShardObject executor, ShardObject item) {
            System.exit(0);
        }
    },

    ERROR {

        @Override
        public boolean consumesAction() { return false; }
    };

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


    /**
     * Does this command constitute as an action (default yes).
     */
    public boolean consumesAction() { return true ; }
}