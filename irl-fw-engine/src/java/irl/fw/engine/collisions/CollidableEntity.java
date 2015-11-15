package irl.fw.engine.collisions;

import irl.fw.engine.entity.Entity;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/15/15
 */
public interface CollidableEntity {

    boolean collide(Entity other);

}
