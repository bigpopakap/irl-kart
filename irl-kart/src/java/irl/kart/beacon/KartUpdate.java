package irl.kart.beacon;

import irl.fw.engine.entity.state.EntityStateUpdate;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/1/15
 */
public class KartUpdate {

    private final String externalId;
    private final EntityStateUpdate stateUpdate;

    public KartUpdate(String externalId, EntityStateUpdate stateUpdate) {
        this.externalId = externalId;
        this.stateUpdate = stateUpdate;
    }

    public String getExternalId() {
        return externalId;
    }

    public EntityStateUpdate getStateUpdate() {
        return stateUpdate;
    }

    @Override
    public String toString() {
        return "Beacon updateEntity for kart: " + getExternalId();
    }
}
