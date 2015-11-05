package irl.fw.engine.events;

import irl.fw.engine.physics.EntityState;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 10/30/15
 */
public class UpdateEntity implements EngineEvent {

    private final String entityId;
    private final EntityState newState;

    public UpdateEntity(String entityId, EntityState newState) {
        this.entityId = entityId;
        this.newState = newState;
    }

    public String getEntityId() {
        return entityId;
    }

    public EntityState getNewState() {
        return newState;
    }

}
