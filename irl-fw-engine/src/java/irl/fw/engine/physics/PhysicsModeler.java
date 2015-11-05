package irl.fw.engine.physics;

import irl.fw.engine.bodies.BodyInstance;
import irl.fw.engine.collisions.CollisionResolver;
import irl.fw.engine.events.AddBody;
import irl.fw.engine.events.RemoveBody;
import irl.fw.engine.events.UpdateBody;
import irl.util.universe.Universe;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/3/15
 */
public interface PhysicsModeler {

    Universe<BodyInstance> getBodies();

    String addBody(AddBody event);
    BodyInstance removeBody(RemoveBody event);
    void updateBody(UpdateBody event);

    void model(CollisionResolver collisionResolver, long timeStep);

}
