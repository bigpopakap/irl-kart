package irl.kart.entities;

import irl.fw.engine.entity.state.EntityStateBuilder;
import irl.fw.engine.geometry.ImmutableShape;
import irl.kart.beacon.KartBeacon;
import irl.fw.engine.entity.IRLEntity;
import irl.fw.engine.entity.state.EntityState;
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
    private final ImmutableShape kartShape;

    public Kart(ImmutableShape shape, String kartId, KartBeacon kartBeacon) {
        this.kartShape = shape;
        this.kartId = kartId;
        this.kartBeacon = kartBeacon;
    }

    @Override
    public Observable<EntityState> updates() {
        //TODO we should only report the latest position or something
        return kartBeacon.updates()
                .filter(update -> StringUtils.equal(kartId, update.getExternalId()))
                .map(update -> new EntityStateBuilder()
                                    .shape(kartShape)
                                    .center(update.getCenter())
                                    .velocity(update.getVelocity())
                                    .build());
    }

}
