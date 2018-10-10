package shard.event;

import shard.*;

/**
 * The Person picks up an Item off the ground.
 */
public class TakeEvent implements Event {

    private static final EventCode code = EventCode.TAKE;
    private Person person;
    private Item item;

    public TakeEvent(Person person, Item item) {
        this.person = person;
        this.item = item;
    }

    public Person getPerson() { return this.person; }
    public Item getItem() { return this.item; }
    public EventCode getEventCode() { return this.code; }
}