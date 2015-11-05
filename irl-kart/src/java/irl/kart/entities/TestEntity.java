package irl.kart.entities;

import irl.fw.engine.beacon.Beacon;
import irl.fw.engine.entity.IRLEntity;
import irl.fw.engine.physics.EntityState;
import irl.util.string.StringUtils;
import rx.Observable;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/1/15
 */
public class TestEntity implements IRLEntity {

    private final String kartId;
    private final Beacon beacon;

    public TestEntity(String kartId, Beacon beacon) {
        this.kartId = kartId;
        this.beacon = beacon;
    }

    @Override
    public Observable<EntityState> updates() {
        //TODO we should only report the latest position or something
        return beacon.updates()
                    .filter(update -> StringUtils.equal(kartId, update.getExternalId()))
                    .map(update -> update.getState());
    }

}
