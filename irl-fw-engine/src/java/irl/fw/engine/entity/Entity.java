package irl.fw.engine.entity;

import irl.fw.engine.collisions.CollidableEntity;
import irl.fw.engine.entity.factory.EntityConfig;
import irl.fw.engine.entity.state.EntityState;
import irl.fw.engine.entity.state.EntityStateUpdate;
import irl.util.reactiveio.EventQueue;
import rx.Observable;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 10/29/15
 */
public abstract class Entity implements CollidableEntity {

    private final EntityId engineId;
    private volatile EntityState state;

    private EventQueue<EntityState> states;

    public Entity(EntityConfig entityConfig, EntityState initState) {
        this.engineId = entityConfig.getId();
        if (this.engineId == null) {
            throw new IllegalArgumentException("This entity must have an ID");
        }

        this.states = new EventQueue<>();

        setState(initState);
    }

    public abstract boolean isVirtual();

    public EntityId getEngineId() {
        return engineId;
    }

    public EntityState getState() {
        return state;
    }

    public Observable<EntityState> getStates() {
        return states.get();
    }

    public synchronized void setState(EntityState state) {
        this.state = state;
        this.states.mergeIn(this.state);
    }

    public synchronized void updateState(EntityStateUpdate stateUpdates) {
        setState(stateUpdates.fillAndBuild(getState()));
    }

    @Override
    public boolean collide(Entity other) {
        return true;
    }

}
