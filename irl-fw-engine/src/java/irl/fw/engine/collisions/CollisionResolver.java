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
     * Gets called when a collision is detected, but before it
     * has been realized.
     *
     * Note that this may get called many times
     *
     * @return true if you want to allow the collision to occur,
     *          false if you want to bypass the collision
     */
    boolean onBeforeCollision(EntityCollision collision);

    /**
     * Gets called when a collision is happening and there's nothing
     * you can do to stop it
     */
    void onCollision(EntityCollision collision);

}
