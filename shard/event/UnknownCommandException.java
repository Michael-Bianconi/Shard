package shard.event;

public class UnknownCommandException extends Exception {
    
    public UnknownCommandException(String s) {
        super(s);
    }
}