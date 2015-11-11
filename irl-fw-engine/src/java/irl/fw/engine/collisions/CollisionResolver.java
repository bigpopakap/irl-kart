package irl.fw.engine.collisions;

import irl.fw.engine.events.EntityCollision;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 10/29/15
 */
public interface CollisionResolver {

    boolean onBeforeCollision(EntityCollision collision);
    void onCollision(EntityCollision collision);

}
