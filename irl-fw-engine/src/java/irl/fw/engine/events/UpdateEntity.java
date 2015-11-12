package irl.fw.engine.events;

import irl.fw.engine.entity.EntityId;
import irl.fw.engine.entity.state.EntityStateUpdate;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 10/30/15
 */
public class UpdateEntity implements EngineEvent {

    private final EntityId entityId;
    private final EntityStateUpdate stateUpdates;

    public UpdateEntity(EntityId entityId, EntityStateUpdate stateUpdate) {
        this.entityId = entityId;
        this.stateUpdates = stateUpdate;
    }

    public EntityId getEntityId() {
        return entityId;
    }

    public EntityStateUpdate getStateUpdate() {
        return stateUpdates;
    }

}
