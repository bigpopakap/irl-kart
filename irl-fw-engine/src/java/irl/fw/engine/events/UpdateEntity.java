package irl.fw.engine.events;

import irl.fw.engine.entity.state.EntityStateUpdate;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 10/30/15
 */
public class UpdateEntity implements EngineEvent {

    private final String entityId;
    private final EntityStateUpdate stateUpdates;

    public UpdateEntity(String entityId, EntityStateUpdate stateUpdate) {
        this.entityId = entityId;
        this.stateUpdates = stateUpdate;
    }

    public String getEntityId() {
        return entityId;
    }

    public EntityStateUpdate getStateUpdate() {
        return stateUpdates;
    }

}
