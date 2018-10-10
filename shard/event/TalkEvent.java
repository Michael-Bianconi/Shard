package shard.event;

import shard.*;

/**
 * Two Persons talk with each other and share information. If either person is
 * the player, then do special stuff. If both are NPCs, then randomly share
 * some information between them.
 */
public class TalkEvent implements Event {

    private static final EventCode code = EventCode.TALK;
    private Person p1; // initiator
    private Person p2; // recipient

    public TalkEvent(Person initiator, Person recipient) {
        this.p1 = initiator;
        this.p2 = recipient;
    }

    public Person getInitiator() { return this.p1; }
    public Person getRecipient() { return this.p2; }
    public EventCode getEventCode() { return this.code; }
}