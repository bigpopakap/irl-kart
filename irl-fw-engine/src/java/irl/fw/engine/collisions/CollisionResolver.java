package irl.fw.engine.collisions;

import irl.fw.engine.events.EntityCollision;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 10/29/15
 */
public interface CollisionResolver {

    /**
     * Gets called when a collision is detected
     * Note that this may get called many times
     *
     * @return true if you want to allow the collision to occur,
     *          false if you want to bypass the collision
     */
    boolean onCollision(EntityCollision collision);

}
