package shard.object;

import java.util.Set;

/**
 * Small interface allowing for the implementation of tags.
 *
 * @author Michael-Bianconi
 * @author https://www.github.com/Michael-Bianconi
 * @version 1
 * @since 15 October 2018
 */
public interface Tagged {
    
    public Set<Tag> getTags();
    public void addTag(Tag t);
    public void removeTag(Tag t);
    public boolean hasTag(Tag t);
}