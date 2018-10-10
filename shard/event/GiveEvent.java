package shard.event;

import shard.*;

/**
 * Person 1 gives an item in his possession to Person 2.
 */
public class GiveEvent implements Event {
    private static final EventCode code = EventCode.GIVE;
    private Person p1;
    private Person p2;
    private Item item;

    public GiveEvent(Person p1, Person p2, Item item) {
        this.p1 = p1;
        this.p2 = p2;
        this.item = item;
    }

    public Person getGiver() { return this.p1; }
    public Person getTaker() { return this.p2; }
    public Item getItem() { return this.item; }
    public EventCode getEventCode() { return this.code; }
}