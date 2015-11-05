package irl.fw.engine.events;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 10/30/15
 */
public class RemoveEntity implements EngineEvent {

    private final String entityId;

    public RemoveEntity(String entityId) {
        this.entityId = entityId;
    }

    public String getEntityId() {
        return entityId;
    }

}
