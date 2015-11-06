package irl.fw.engine.physics.impl.noop;

import irl.fw.engine.entity.Entity;
import irl.fw.engine.entity.state.EntityState;
import irl.fw.engine.collisions.CollisionResolver;
import irl.fw.engine.entity.EntityInstance;
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

    private final Universe<EntityInstance> universe;

    public NoopPhysicsModeler() {
        universe = new Universe<>();
    }

    @Override
    public World getWorld() {
        return new SimpleWorld(universe.toCollection(), -100, 100, -100, 100);
    }

    @Override
    public String add(AddEntity add) {
        Entity newEntity = add.getEntity();
        EntityState initialState = add.getInitialState();

        EntityInstance entityInstance = new EntityInstance(
            newEntity,
            initialState
        );
        return universe.add(entityInstance);
    }

    @Override
    public void remove(RemoveEntity remove) {
        String entityId = remove.getEntityId();

        if (universe.contains(entityId)) {
            universe.remove(entityId);
        } else {
            System.err.println("Tried to remove non-existent entity: " + entityId);
        }
    }

    @Override
    public void update(UpdateEntity update) {
        String entityId = update.getEntityId();
        EntityStateUpdate stateUpdate = update.getStateUpdate();

        if (universe.contains(entityId)) {
            EntityInstance current = universe.get(entityId);
            EntityInstance updated = current.updateState(stateUpdate);
            universe.update(entityId, updated);
        } else {
            System.err.println("Tried to update non-existent entity: " + entityId
                    + " to new state: " + stateUpdate);
        }
    }

    @Override
    public void model(CollisionResolver collisionResolver, long timeStep) {
        //do nothing
    }

}
