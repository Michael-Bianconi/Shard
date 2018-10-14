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
public class Event {

    private final Command command;
    private final ShardObject argument;

    // constructors ============================================================

    public Event(Command command) {
        this(command, null);
    }

    public Event(Command command, ShardObject argument) {
        this.command = command;
        this.argument = argument;
    }

    // accessors ===============================================================

    public Command getCommand() { return this.command; }
    public ShardObject getArgument() { return this.argument; }
    public void execute(ShardObject exec) { command.execute(exec, argument); }

    // override methods ========================================================

    @Override
    public int hashCode() { return Objects.hash(command, argument); }

    @Override
    public boolean equals(Object other) {

        if (this == other) { return true; }
        if (!(other instanceof Event)) { return false; }
        Event e = (Event) other;
        return this.command == e.command && this.argument.equals(e.argument);
    }

    @Override
    public String toString() {
        return command.name() + " " + argument.getName().toUpperCase();
    }
}