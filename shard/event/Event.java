/**
 * Shard
 *
 * Shard is driven by Events, - instances where
 */

package shard.event;

public interface Event {

    public EventCode getEventCode();
    
}