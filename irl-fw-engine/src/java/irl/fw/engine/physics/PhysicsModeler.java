package irl.fw.engine.physics;

import irl.fw.engine.collisions.CollisionResolver;
import irl.fw.engine.events.AddEntity;
import irl.fw.engine.events.RemoveEntity;
import irl.fw.engine.events.UpdateEntity;
import irl.fw.engine.world.World;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/3/15
 */
public interface PhysicsModeler {

    World getWorld();

    void addEntity(AddEntity add);
    void removeEntity(RemoveEntity remove);
    void updateEntity(UpdateEntity update);

    void model(CollisionResolver collisionResolver, long timeStep);

}
