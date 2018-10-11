import shard.*;
import shard.event.*;
import shard.object.*;
import shard.user.*;
import java.util.Scanner;


/**
 * Note:
 *
 * Be careful with naming conventions. If you name two non-Item ShardObjects
 * the same thing, there may be some funkiness.
 *
 * TODO: Clean up the input manager
 */
public class ShardExe {
    public static void main(String args[]) {
        Room nullroom = Room.NullRoom();
        Room lounge = new Room("Lounge");
        Room kitchen = new Room("Kitchen");
        Item hammer = new Item("Hammer", lounge);
        Item globe = new Item("Globe", lounge);
        Item pan = new Item("Pan", kitchen);
        Item knife = new Item("Knife", kitchen);
        Person fred = new Person("Fred Barnes", nullroom);
        Person lisa = new Person("Lisa Fredrickson", kitchen);
        Player mike = new Player("Mike", lounge);
        Item rope = new Item("Rope", mike);
        InputManager inManager = new InputManager(mike);

        hammer.setDescription("A rusty old hammer.");
        hammer.setState(ItemState.BLOODY);

        globe.setDescription("A globe of the world.");
        fred.setDescription("A slim-built man with round glasses.");
        lisa.setDescription("A tall, portly woman who's hair smells like vanilla.");
        mike.setDescription("The hero of our story.");
        lounge.setDescription("A brightly lit room with several couches.");
        kitchen.setDescription("A dim kitchen with pots, pan, and many knives.");

        lounge.connectRoom(kitchen);
        kitchen.connectRoom(lounge);

        while(true) {
            Scanner s = new Scanner(System.in);
            String command = s.nextLine();

            try {
                System.out.println(inManager.parse(command));
            } catch (CommandNotFoundException e) {
                System.out.println("Command not found!");
            } catch (ArgumentNotFoundException e) {
                System.out.println("Arguments not found!");
            }
        }
    }
}