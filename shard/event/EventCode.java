/**
 * Shard
 * @author Michael Bianconi
 *
 * List of different EventCodes. If this file is changed, be sure to
 * update InputManager and EventManager.
 */

package shard.event;

public enum EventCode {
    ENTER,          // GOTO [Room]
    DESCRIBE,       // DESCRIBE [ShardObject]
    TAKE,           // TAKE [Item]
    DROP,           // DROP [Item]
    INVESTIGATE,    // INVESTIGATE [Room]
    TALK,           // TALK [Person]
    WHOAMI,         // WHOAMI
    WHEREAMI,       // WHEREAMI

    ERROR           // ERROR
}