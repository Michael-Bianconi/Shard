/**
 * Shard
 * @author Michael Bianconi
 */

 package shard.event;
 import shard.object.ShardObject;
 import shard.object.Room;
 import shard.object.Owner;
 import java.util.ArrayList;

public class EventManager {
    
    public static void execute(Event e) throws MissingArgumentException,
                                               UnknownCommandException {

        try {
            ShardObject arg = e.getObjects().get(0);

            switch(e.getEventCode()) {
                case ENTER:
                    e.getExecutor().setLocation((Owner) arg);
                    break;

                case DESCRIBE:
                    System.out.println(arg.getDescription());
                    break;

                case TAKE:
                    arg.setLocation((Owner) e.getExecutor());
                    break;

                case DROP:
                    arg.setLocation(e.getExecutor().getLocation());
                    break;

                case INVESTIGATE:

                    System.out.println("You see the following objects: ");
                    Room r = (arg instanceof Room) ? (Room) arg : new Room();
                    for (ShardObject o : r.getObjects()) {
                        System.out.println(o);
                    }

                    break;

                case TALK:
                    // do nothing (for now)
                    break;

                default:
                    throw new UnknownCommandException("Unknown EventCode: " +
                                                      e.getEventCode().name());
            }
        } catch (IndexOutOfBoundsException exception) {
            throw new MissingArgumentException("Missing an argument in: " +
                                               exception.toString());
        }
    }
}