/**
 * Shard
 * @author Michael Bianconi
 */

package shard.event;
import shard.object.ShardObject;
import java.util.ArrayList;
import java.util.Objects;

public class Event {

    private EventCode code;
    private ArrayList<ShardObject> objects;

    // CONSTRUCTOR =============================================================

    public Event(EventCode code, ArrayList<ShardObject> objects) {
        this.code = code;
        this.objects = objects;
    }

    // ACCESSORS ===============================================================

    public EventCode getEventCode() { return this.code; }
    public ArrayList<ShardObject> getObjects() { return this.objects; }

    // OVERRIDDEN METHODS ======================================================
    @Override
    public String toString() { return this.code.name(); }

    @Override
    public int hashCode() { return Objects.hash(this.code, this.objects); }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (!(o instanceof Event)) { return false; }

        Event e = (Event) o;

        return this.code == e.code && this.objects.equals(e.objects);
    }
}