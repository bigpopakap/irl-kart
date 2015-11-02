package irl.fw.beacon;

import irl.fw.shared.bodies.PhysicalState;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/1/15
 */
public class BeaconUpdate {

    private final String externalId;
    private final PhysicalState state;

    public BeaconUpdate(String externalId, PhysicalState state) {
        this.externalId = externalId;
        this.state = state;
    }

    public String getExternalId() {
        return externalId;
    }

    public PhysicalState getState() {
        return state;
    }

    @Override
    public String toString() {
        return getExternalId() + ":" + getState();
    }
}
