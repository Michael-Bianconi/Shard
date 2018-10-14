package shard.object;
import java.util.ArrayList;

/**
 * This interface allows implementers to store "memories" that can be
 * shared with the player.
 *
 * @author Michael Bianconi
 * @author https://www.github.com/Michael-Bianconi
 * @version 1
 * @since 14 October 2018
 */
public interface Memory {
    public ArrayList<String> getMemories();
    public String getLatestMemory();
    public void addMemory(String m);
}