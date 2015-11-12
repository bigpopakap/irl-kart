package irl.kart.events.beacon;

import irl.fw.engine.entity.state.EntityStateUpdate;
import irl.kart.beacon.KartBeaconEvent;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/1/15
 */
public class KartStateUpdate extends KartBeaconEvent {

    private final EntityStateUpdate stateUpdate;

    public KartStateUpdate(String kartId, EntityStateUpdate stateUpdate) {
        super(kartId);
        this.stateUpdate = stateUpdate;
    }

    public EntityStateUpdate getStateUpdate() {
        return stateUpdate;
    }

}
