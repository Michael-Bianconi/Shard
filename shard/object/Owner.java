/**
 * Shard
 * @author Michael Bianconi
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