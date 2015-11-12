package irl.fw.engine.events;

import irl.fw.engine.entity.EntityId;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 10/30/15
 */
public class RemoveEntity implements EngineEvent {

    private final EntityId entityId;

    public RemoveEntity(EntityId entityId) {
        this.entityId = entityId;
    }

    public EntityId getEntityId() {
        return entityId;
    }

}
