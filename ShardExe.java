import shard.*;
import shard.event.*;
import shard.object.*;
import shard.user.*;
import java.util.Scanner;


/**
 * Note:
 *
 * Be careful with naming conventions. If you name two items the same name,
 * there may be unavoidable funkiness. For example, if there are two Lukes,
 * and the player says "Talk to Luke", there might be an issue.
 *
 * For Items it's a bit simpler. If 2 items have same name and state, they
 * should have the same description as well.
 *
 * I've run into an issue. To compare 2 people, I should (but not necessarily)
 * compare their inventories. Comparing inventories involves checking each
 * Item they're holding. To compare Items, I have to check if they're in the
 * same location.
 */
public class ShardExe {

    public static void main(String args[]) {

        test(args);
    }





    public static void test(String args[]) {

        // creating rooms
        Room lounge = new Room("lounge");
        Room kitchen = new Room("kitchen");
        Room.connectRooms(lounge, kitchen);

        Room foyer = new Room("foyer");
        Room.connectRooms(lounge, foyer);

        Room basement = new Room("basement");
        Room.connectRooms(kitchen, basement);

        Room mainHall = new Room("main hall");
        Room.connectRooms(foyer, mainHall);

        Room masterBedroom = new Room("master bedroom");
        Room.connectRooms(masterBedroom, mainHall);
        basement.connectRoom(masterBedroom); // secret passage!

        // adding objects
        Item kitchenKnife = new Item("kitchen knife", kitchen);
        Item globe = new Item("globe", lounge);
        Item bust = new Item("statue bust", foyer);
        Item wine = new Item("wine", basement);
        Item bedsheet = new Item("bedsheet", masterBedroom);

        // creating people
        Guest fred = new Guest("Fred Barnes", lounge);
        Guest lisa = new Guest("Lisa Fredrickson", kitchen);
        Guest mark = new Guest("Mark Twain", lounge);
        Guest luke = new Guest("Luke Vanmont", masterBedroom);

        // creating the player
        Player mike = new Player("Mike", lounge);
        InputManager inManager = new InputManager(mike);

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