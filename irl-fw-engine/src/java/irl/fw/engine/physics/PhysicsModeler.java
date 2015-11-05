package irl.fw.engine.physics;

import irl.fw.engine.entity.EntityInstance;
import irl.fw.engine.collisions.CollisionResolver;
import irl.fw.engine.events.AddEntity;
import irl.fw.engine.events.RemoveEntity;
import irl.fw.engine.events.UpdateEntity;
import irl.util.universe.Universe;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/3/15
 */
public interface PhysicsModeler {

    Universe<EntityInstance> getEntities();

    String add(AddEntity event);
    EntityInstance remove(RemoveEntity event);
    void update(UpdateEntity event);

    void model(CollisionResolver collisionResolver, long timeStep);

}
