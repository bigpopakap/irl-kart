package irl.fw.engine.physics.impl.noop;

import irl.fw.engine.entity.Entity;
import irl.fw.engine.collisions.CollisionResolver;
import irl.fw.engine.entity.EntityId;
import irl.fw.engine.entity.state.EntityStateUpdate;
import irl.fw.engine.events.AddEntity;
import irl.fw.engine.events.RemoveEntity;
import irl.fw.engine.events.UpdateEntity;
import irl.fw.engine.physics.PhysicsModeler;
import irl.fw.engine.world.SimpleWorld;
import irl.fw.engine.world.World;
import irl.util.universe.Universe;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/3/15
 */
public class NoopPhysicsModeler implements PhysicsModeler {

    private final Universe<Entity> universe;

    public NoopPhysicsModeler() {
        universe = new Universe<>();
    }

    @Override
    public World getWorld() {
        return new SimpleWorld(universe.toCollection(), -100, 100, -100, 100);
    }

    @Override
    public void addEntity(AddEntity add) {
        universe.add(add.getEntityFactory());
    }

    @Override
    public void removeEntity(RemoveEntity remove) {
        EntityId entityId = remove.getEntityId();

        if (universe.contains(entityId.toString())) {
            universe.remove(entityId.toString());
        } else {
            System.err.println("Tried to removeEntity non-existent entity: " + entityId);
        }
    }

    @Override
    public void updateEntity(UpdateEntity update) {
        EntityId entityId = update.getEntityId();
        EntityStateUpdate stateUpdate = update.getStateUpdate();

        if (universe.contains(entityId.toString())) {
            Entity entity = universe.get(entityId.toString());
            entity.updateState(stateUpdate);
            universe.update(entityId.toString(), entity);
        } else {
            System.err.println("Tried to updateEntity non-existent entity: " + entityId
                    + " to new state: " + stateUpdate);
        }
    }

    @Override
    public void model(CollisionResolver collisionResolver, long timeStep) {
        //do nothing
    }

}
