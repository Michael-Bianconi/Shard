import shard.object.*;
import shard.user.*;
import shard.event.*;
import shard.ai.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Shard {

    public static void main(String[] args) {

        // Rooms
        Room lounge =
            new Room("the lounge",
                     "A large room with several chairs and couches.");

        Room kitchen =
            new Room("the kitchen",
                     "A fully-furnished kitchen with very clean countertops.");

        Room hallway =
            new Room("the hallway",
                     "A long hallway with several doorways.");

        Room masterBed =
            new Room("the master bedroom",
                     "A large bedroom with several wall-sized paintings.");

        Room childBed =
            new Room("the child's bedroom",
                     "A smaller bedroom with toys strewn about.");

        Room guestBed =
            new Room("the guest room",
                     "A modest bedroom with several amenities.");

        Room cellar =
            new Room("the cellar",
                     "A dark basement filled with wine racks.");

        Room diningRoom =
            new Room("the dining room",
                     "A large dining room with a long oak table.");

        Room study =
            new Room("the study",
                     "A quiet room filled with books.");

        Room secretPassage =
            new Room ("a secret passage",
                      "A small, hidden tunnel.");

        Room.connectRooms(lounge, kitchen);
        Room.connectRooms(lounge, hallway);
        Room.connectRooms(lounge, diningRoom);
        Room.connectRooms(kitchen, cellar);
        Room.connectRooms(kitchen, diningRoom);
        Room.connectRooms(hallway, masterBed);
        Room.connectRooms(hallway, guestBed);
        Room.connectRooms(hallway, childBed);
        Room.connectRooms(hallway, study);

        // Guests
        ArrayList<Guest> guests = new ArrayList<Guest>();
        
        Guest kahn =
            new Guest("Madeline Kahn",
                      "A young blonde woman with brown eyes.",
                      lounge);

        Guest warren =
            new Guest("Lesley Ann Warren",
                      "A skinny woman with brown hair.",
                      lounge);

        Guest lloyd =
            new Guest("Christopher Lloyd",
                      "An old man with glasses.",
                      lounge);

        Guest brennan =
            new Guest("Eileen Brennan",
                      "A middle-aged red-head.",
                      lounge);

        Guest mull =
            new Guest("Martin Mull",
                      "An old gentleman.",
                      lounge);

        Guest mckean =
            new Guest("Michael McKean",
                      "A large man with gray hair.",
                      lounge);

        guests.add(kahn);
        guests.add(warren);
        guests.add(lloyd);
        guests.add(brennan);
        guests.add(mull);
        guests.add(mckean);

        Guest murderer = randomlySelectMurderer(guests);

        // Items
        Item globe =
            new Item("a globe",
                     "A globe of the Earth.",
                     lounge);

        Item bust =
            new Item("a statue bust",
                     "A bust of John F. Kennedy.",
                     lounge);

        Item knife =
            new Item("a knife",
                     "A large kitchen knife.",
                     kitchen);

        Item tenderizer =
            new Item("a meat tenderizer",
                     "A heavy, spiked meat tenderizer.",
                     kitchen);

        Item winebottle =
            new Item("a wine bottle",
                     "A '73 Riesling.",
                     cellar);

        Item lever =
            new Item("a lever",
                     "A small lever connected to the wall",
                     cellar);

        Item button =
            new Item("a button",
                     "A small red button on the wall.",
                     secretPassage);

        Item rope =
            new Item("a rope",
                     "A sturdy rope.",
                     masterBed);

        Item snowglobe =
            new Item("a snow globe",
                     "A small, heavy snowglobe.",
                     masterBed);

        Item toychest =
            new Item("a toy chest",
                     "A large wooden chest filled with toys.",
                     childBed);

        Item doll =
            new Item("a doll",
                     "A small doll with blonde hair.",
                     childBed);

        Item candlestick =
            new Item("a candlestick",
                     "A heavy silver candlestick",
                     diningRoom);

        Item letterOpener =
            new Item("a letter opener",
                     "A small, sharp letter opener.",
                     study);

        Item mirror =
            new Item("a mirror",
                     "A large mirror hanging on the wall",
                     guestBed);

        bust.setWeapon(true);
        knife.setWeapon(true);
        tenderizer.setWeapon(true);
        winebottle.setWeapon(true);
        rope.setWeapon(true);
        snowglobe.setWeapon(true);
        candlestick.setWeapon(true);
        letterOpener.setWeapon(true);

        bust.setMovable(true);
        knife.setMovable(true);
        tenderizer.setMovable(true);
        winebottle.setMovable(true);
        rope.setMovable(true);
        snowglobe.setMovable(true);
        candlestick.setMovable(true);
        letterOpener.setMovable(true);
        doll.setMovable(true);

        lever.setUsable(true);
        lever.setOnUseEvent(Command.ENTER, secretPassage);

        button.setUsable(true);
        button.setOnUseEvent(Command.ENTER, masterBed);

        Player player =
            new Player("You",
                       "Master of the universe.",
                       lounge);

        Scanner in = new Scanner(System.in);

        System.out.println("\n\nShard\n\nTry to find the murderer before they"
                         + " kill everyone!\n\n");

        // main loop
        while(true) {

            boolean playerConsumedAction = false;
            boolean allGuestsDead = false;

            // until the player does something to initiate the next turn
            while (!playerConsumedAction) {

                // get input
                System.out.print(OutputType.USER_INPUT_REQUESTED.prefix() + " ");
                String input = in.nextLine();

                // try to execute command
                try {
                    Event e = InputManager.parse(player, input);
                    if (e.getCommand().consumesAction()) { 
                        playerConsumedAction = true;
                    }
                    System.out.println(OutputManager.format(e));

                    e.execute();

                } catch (InvalidInputException e) {
                    System.out.println(e.getMessage());
                }
            }

            // for each guest, determine their next action
            for (Guest g : guests) {
                if (!g.isDead() && !g.equals(murderer)) {
                    allGuestsDead = false;
                    Event e = BehaviorManager.guestAction(g);
                    if (e.getCommand() != Command.ERROR) {

                        // print out if in vicinity of the player
                        if (g.getLocation().equals(player.getLocation())) {
                            System.out.println(OutputManager.format(e));
                        }

                        e.execute();

                        // print out if in vicinity of the player
                        if (e.getCommand() == Command.ENTER
                        &&  g.getLocation().equals(player.getLocation())) {
                            
                            System.out.println(OutputManager.format(e));
                        }
                    }
                }

                else if (g.equals(murderer)) {
                    // murderer action
                    Event e = BehaviorManager.murdererAction(murderer);

                    if (e.getCommand() != Command.ERROR) {
                        if (murderer.getLocation().equals(player.getLocation())) {
                            System.out.println(OutputManager.format(e));
                        }
                        e.execute();
                    }
                }
            }

            // if all guests are dead, it's game over
            if (allGuestsDead) {
                System.out.println("The murderer, " + murderer.getName()
                                +  " has killed everybody!");

                break; 
            }
        }
    }




    private static Guest randomlySelectMurderer(ArrayList<Guest> guests) {

        Random rand = new Random();
        int index = rand.nextInt(guests.size());
        guests.get(index).setIsMurderer(true);
        return guests.get(index);

    }
}