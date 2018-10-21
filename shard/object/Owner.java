/**
 * Interface that allows implementers to hold other ShardObjects.
 *
 * @author Michael Bianconi
 * @author https://www.github.com/Michael-Bianconi
 */

package shard.object;

import java.util.ArrayList;

/**
 * Owners can have possession of objects.
 */
public interface Owner {
    public void addObject(ShardObject o);
    public void removeObject(ShardObject o);
    public boolean hasObject(ShardObject o);
    public ArrayList<ShardObject> getObjects();
}