import shard.*;
import shard.event.*;
import shard.object.*;

public class ShardExe {
    public static void main(String args[]) {
        Room lounge = new Room("Lounge");
        Room kitchen = new Room("Kitchen");
        Item hammer = new Item("Hammer", lounge);
        Item globe = new Item("Globe", lounge);
        Item pan = new Item("Pan", kitchen);
        Item knife = new Item("Knife", kitchen);
        Person fred = new Person("Fred Barnes", lounge);
        Person lisa = new Person("Lisa Fredrickson", kitchen);

        hammer.setDescription("A rusty old hammer.");
        hammer.setState(ItemState.BLOODY);

        globe.setDescription("A globe of the world.");
        fred.setDescription("A slim-built man with round glasses.");
        lisa.setDescription("A tall, portly woman who's hair smells like vanilla.");
        lounge.setDescription("A brightly lit room with several couches.");
        kitchen.setDescription("A dim kitchen with pots, pan, and many knives.");


        lounge.connectRoom(kitchen);
        kitchen.connectRoom(lounge);

        System.out.println(lounge.getName());
        System.out.println(lounge.getDescription());
        for (ShardObject o : lounge.getObjects()) {
            System.out.println(o);
        }

        System.out.println("");

        System.out.println(kitchen.getName());
        System.out.println(kitchen.getDescription());
        for (ShardObject o : kitchen.getObjects()) {
            System.out.println(o);
        }

        knife.setLocation(lisa);

        System.out.println("");

        System.out.println(kitchen.getName());
        System.out.println(kitchen.getDescription());
        for (ShardObject o : kitchen.getObjects()) {
            System.out.println(o);
        }

        System.out.println("");

        System.out.println(lisa.getName());
        System.out.println(lisa.getDescription());
        for (ShardObject o : lisa.getObjects()) {
            System.out.println(o);
        }
    }
}