/**
 * Shard
 * @author Michael Bianconi
 */

package shard.event;

public enum EventCode {
    GOTO,           // GOTO [Room]
    DESCRIBE,       // DESCRIBE [ShardObject]
    TAKE,           // TAKE [Item]
    DROP,           // DROP [Item]
    INVESTIGATE,    // INVESTIGATE [Room]
    TALK,           // TALK [Person]
    ERROR           // ERROR
}