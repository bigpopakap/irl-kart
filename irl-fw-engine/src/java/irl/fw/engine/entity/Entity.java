package irl.fw.engine.entity;

import irl.fw.engine.entity.factory.EntityConfig;
import irl.fw.engine.entity.state.EntityState;
import irl.fw.engine.entity.state.EntityStateUpdate;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 10/29/15
 */
public abstract class Entity {

    private final EntityId engineId;
    private volatile EntityState state;

    public Entity(EntityConfig entityConfig, EntityState initState) {
        this.engineId = entityConfig.getId();
        if (this.engineId == null) {
            throw new IllegalArgumentException("This entity must have an ID");
        }

        setState(initState);
    }

    public abstract boolean isVirtual();

    public EntityId getEngineId() {
        return engineId;
    }

    public EntityState getState() {
        return state;
    }

    public synchronized void setState(EntityState state) {
        this.state = state;
    }

    public synchronized void updateState(EntityStateUpdate stateUpdates) {
        setState(stateUpdates.fillAndBuild(getState()));
    }

}
