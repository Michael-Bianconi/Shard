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
    private ArrayList<ShardObject> objects;

    // CONSTRUCTOR =============================================================

    public Event(EventCode code) {
        this(code, new ArrayList<ShardObject>());
    }

    public Event(EventCode code, ShardObject object) {
        this(code);
        this.objects.add(object);
    }

    public Event(EventCode code,
                 ArrayList<ShardObject> objects) {
        this.code = code;
        this.objects = objects;
    }

    public static Event ErrorEvent() {
        return new Event(EventCode.ERROR);
    }

    // ACCESSORS ===============================================================

    public EventCode getEventCode() { return this.code; }
    public ArrayList<ShardObject> getObjects() { return this.objects; }

    // OVERRIDDEN METHODS ======================================================
    @Override
    public String toString() {

        String str = this.code.name() + " ";

        for (ShardObject o : this.objects) {
            str += o.toString().toUpperCase() + " ";
        }

        return str;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.code, this.objects);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (!(o instanceof Event)) { return false; }

        Event e = (Event) o;

        return this.code == e.code
            && this.objects.equals(e.objects);
    }
}