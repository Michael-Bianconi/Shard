import shard.*;
import shard.event.*;
import shard.object.*;

public class ShardExe {
    public static void main(String args[]) {
        Item testroom = new Item("Room", null);
        Item hammer = new Item("Hammer", testroom);
        hammer.setDescription("A rusty old hammer.");
        hammer.setState(ItemState.DAMAGED);

        System.out.println(hammer.getName());
        System.out.println(hammer.getDescription());
        System.out.println(hammer.getItemState());

        System.out.println(hammer.hashCode());
        System.out.println(testroom.hashCode());
    }
}