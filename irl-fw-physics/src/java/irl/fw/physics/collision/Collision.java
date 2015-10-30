package irl.fw.physics.collision;

import irl.fw.physics.bodies.Body;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 10/29/15
 */
public interface Collision {

    Body getFirst();
    Body getSecond();

    void resolve(CollisionResolver resolver);

}
