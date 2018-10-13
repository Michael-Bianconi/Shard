/**
 * Shard
 * @author Michael Bianconi
 *
 * Events hold an action (EventCode), executor, and a list of relevent
 * ShardObjects.
 *
 * This class itself does not check to make sure events make sense. For example,
 * an event can be something like DESCRIBE KNIFE KITCHEN, which would be
 * executed as - describing the kitchen to the knife.
 */

package shard.event;
import shard.object.ShardObject;
import java.util.ArrayList;
import java.util.Objects;

public class Event {

    private EventCode code;
    private ShardObject executor;
    private ArrayList<ShardObject> objects;

    // CONSTRUCTOR =============================================================

    public Event(EventCode code,
                 ShardObject executor,
                 ArrayList<ShardObject> objects) {
        this.code = code;
        this.executor = executor;
        this.objects = objects;
    }

    public static Event ErrorEvent() {
        return new Event(EventCode.ERROR, null, new ArrayList<ShardObject>());
    }

    // ACCESSORS ===============================================================

    public EventCode getEventCode() { return this.code; }
    public ShardObject getExecutor() { return this.executor; }
    public ArrayList<ShardObject> getObjects() { return this.objects; }

    // OVERRIDDEN METHODS ======================================================
    @Override
    public String toString() {

        String str = this.code.name() + " "
                   + this.executor.toString().toUpperCase() + " ";

        for (ShardObject o : this.objects) {
            str += o.toString().toUpperCase() + " ";
        }

        return str;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.code, this.executor, this.objects);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (!(o instanceof Event)) { return false; }

        Event e = (Event) o;

        return this.code == e.code
            && this.executor.equals(e.executor)
            && this.objects.equals(e.objects);
    }
}