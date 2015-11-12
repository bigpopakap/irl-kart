package irl.fw.engine.entity;

import irl.fw.engine.entity.state.EntityState;
import irl.fw.engine.entity.state.EntityStateUpdate;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 10/29/15
 */
public abstract class Entity {

    private final String engineId;
    private EntityState state;

    public Entity(String engineId, EntityState initState) {
        this.engineId = engineId;
        setState(initState);
    }

    public abstract boolean isVirtual();

    public String getEngineId() {
        return engineId;
    }

    public EntityState getState() {
        return state;
    }

    public void setState(EntityState state) {
        this.state = state;
    }

    public void updateState(EntityStateUpdate stateUpdates) {
        setState(stateUpdates.fillAndBuild(getState()));
    }

}
