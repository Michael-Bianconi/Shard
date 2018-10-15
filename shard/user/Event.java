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

    private final ShardObject executor;
    private final Command command;
    private final ShardObject argument;

    // constructors ============================================================

    public Event(Command command, ShardObject executor) {
        this(command, executor, null);
    }

    public Event(Command command, ShardObject executor, ShardObject argument) {
        
        this.executor = executor;
        this.command = command;
        this.argument = argument;
    }

    // accessors ===============================================================

    public Command getCommand() { return this.command; }
    public ShardObject getArgument() { return this.argument; }
    public ShardObject getExecutor() { return this.executor; }
    public void execute() { command.execute(executor, argument); }
    public String toMemory() { return OutputManager.format(this); }

    // override methods ========================================================

    @Override
    public int hashCode() { return Objects.hash(command, argument, executor); }

    @Override
    public boolean equals(Object other) {

        if (this == other) { return true; }
        if (!(other instanceof Event)) { return false; }
        Event e = (Event) other;
        return this.command == e.command
            && this.argument.equals(e.argument)
            && this.executor.equals(e.executor);
    }

    @Override
    public String toString() {

        String str = executor.getName().toUpperCase() + " " + command.name();
        if (argument != null) { str += " " + argument.getName().toUpperCase(); }

        return str;
    }
}