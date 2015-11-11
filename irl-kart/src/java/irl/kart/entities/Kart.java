package irl.kart.entities;

import irl.fw.engine.entity.state.EntityStateUpdate;
import irl.fw.engine.geometry.ImmutableShape;
import irl.kart.beacon.KartBeacon;
import irl.fw.engine.entity.IRLEntity;
import irl.kart.events.kart.SpinKart;
import irl.util.string.StringUtils;
import rx.Observable;

import java.awt.*;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/1/15
 */
public class Kart extends IRLEntity {

    public static final ImmutableShape SHAPE = new ImmutableShape(
        ImmutableShape.Type.POLYGON,
        new Polygon(
            new int[] { 10, 10,  5,  0, 0},
            new int[] {  0, 15, 20, 15, 0},
            5
        )
    );

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

    public void spin() {
        kartBeacon.send(new SpinKart(kartId));
    }

}
