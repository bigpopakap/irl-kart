package irl.fw.engine.physics.impl.noop;

import irl.fw.engine.entity.Entity;
import irl.fw.engine.entity.state.EntityState;
import irl.fw.engine.collisions.CollisionResolver;
import irl.fw.engine.events.AddEntity;
import irl.fw.engine.events.RemoveEntity;
import irl.fw.engine.events.UpdateEntity;
import irl.fw.engine.entity.EntityInstance;
import irl.fw.engine.physics.PhysicsModeler;
import irl.util.universe.Universe;

import java.util.Collection;

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
    public Collection<EntityInstance> getEntities() {
        return universe.toCollection();
    }

    @Override
    public String add(AddEntity event) {
        Entity entityToAdd = event.getEntity();
        EntityInstance entityInstance = new EntityInstance(
                entityToAdd,
                event.getInitialState()
        );
        return universe.add(entityInstance);
    }

    @Override
    public void remove(RemoveEntity event) {
        String entityToRemove = event.getEntityId();
        if (universe.contains(entityToRemove)) {
            universe.remove(entityToRemove);
        } else {
            System.err.println("Tried to remove non-existent entity: " + entityToRemove);
        }
    }

    @Override
    public void update(UpdateEntity event) {
        String entityToUpdate = event.getEntityId();
        EntityState newState = event.getNewState();

        if (universe.contains(entityToUpdate)) {
            EntityInstance current = universe.get(entityToUpdate);
            EntityInstance updated = current.setState(newState);
            universe.update(entityToUpdate, updated);
        } else {
            System.err.println("Tried to update non-existent entity: " + entityToUpdate
                    + " to new state: " + newState);
        }
    }

    @Override
    public void model(CollisionResolver collisionResolver, long timeStep) {
        //do nothing
    }

}
