package irl.fw.engine.beacon;

import irl.fw.engine.entity.EntityState;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/1/15
 */
public class BeaconUpdate {

    private final String externalId;
    private final EntityState state;

    public BeaconUpdate(String externalId, EntityState state) {
        this.externalId = externalId;
        this.state = state;
    }

    public String getExternalId() {
        return externalId;
    }

    public EntityState getState() {
        return state;
    }

    @Override
    public String toString() {
        return getExternalId() + ":" + getState();
    }
}
