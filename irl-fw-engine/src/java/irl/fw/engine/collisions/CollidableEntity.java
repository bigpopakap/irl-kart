package irl.fw.engine.collisions;

import irl.fw.engine.entity.Entity;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/15/15
 */
public interface CollidableEntity {

    default boolean collide(Entity other) {
        return true;
    }

    default void afterCollide(Entity other) {
        //do nothing
    }

}
