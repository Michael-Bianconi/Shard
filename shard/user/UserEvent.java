package shard.user;
import shard.object.*;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Shard-readable data that holds user commands
 * @author Michael-Bianconi
 * @author https://www.github.com/Michael-Bianconi
 * @version 2
 * @since 14 October 2018
 */
public class UserEvent {

    private final Command command;
    private final ArrayList<ShardObject> arguments;

    // constructors ============================================================

    public UserEvent(Command command) {
        this(command, new ArrayList<ShardObject>());
    }

    public UserEvent(Command command, ShardObject object) {
        this(command);
        this.arguments.add(object);
    }

    public UserEvent(Command command, ArrayList<ShardObject> list) {
        this.command = command;
        this.arguments = list;
    }

    // accessors ===============================================================

    public Command getCommand() { return this.command; }
    public ArrayList<ShardObject> getArguments() { return this.arguments; }
    public int getNumArguments() { return this.arguments.size(); }

    // override methods ========================================================

    @Override
    public int hashCode() { return Objects.hash(command, arguments); }

    @Override
    public boolean equals(Object other) {

        if (this == other) { return true; }
        if (!(other instanceof UserEvent)) { return false; }
        UserEvent e = (UserEvent) other;
        return this.command == e.command && this.arguments.equals(e.arguments);
    }

    @Override
    public String toString() {

        String str = command.name() + " ";
        for (ShardObject o : arguments) {
            str += o.getName().toUpperCase() + " ";
        }

        return str;
    }
}