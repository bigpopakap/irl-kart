package irl.kart.entities;

import irl.fw.engine.entity.state.EntityStateUpdate;
import irl.kart.beacon.KartBeacon;
import irl.fw.engine.entity.IRLEntity;
import irl.util.string.StringUtils;
import rx.Observable;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/1/15
 */
public class Kart implements IRLEntity {

    private final String kartId;
    private final KartBeacon kartBeacon;

    public Kart(String kartId, KartBeacon kartBeacon) {
        this.kartId = kartId;
        this.kartBeacon = kartBeacon;
    }

    @Override
    public Observable<EntityStateUpdate> updates() {
        //TODO we should only report the latest position or something
        return kartBeacon.updates()
                .filter(update -> StringUtils.equal(kartId, update.getExternalId()))
                .map(update -> update.getStateUpdate());
    }

}
