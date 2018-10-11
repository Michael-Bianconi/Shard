package shard.user;

public class CommandNotFoundException extends Exception {
    
    public CommandNotFoundException(String s) {
        super(s);
    }
}