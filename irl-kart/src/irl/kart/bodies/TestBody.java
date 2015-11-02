package irl.kart.bodies;

import irl.fw.physics.bodies.IRLBody;
import irl.fw.physics.events.UpdateBody;
import irl.fw.physics.world.PhysicalState;
import irl.kart.beacon.KartBeacon;
import irl.util.string.StringUtils;
import rx.Observable;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/1/15
 */
public class TestBody implements IRLBody {

    private final String externalId;
    private KartBeacon beacon;

    public TestBody(String externalId, KartBeacon beacon) {
        this.externalId = externalId;
        this.beacon = beacon;
    }

    @Override
    public Observable<UpdateBody> updates(String bodyId) {
        //TODO we should only report the latest position or something
        return beacon.updates()
                     .filter(update -> StringUtils.equal(externalId, update.getKartId()))
                     .map(update -> new PhysicalState(update.getPosition()))
                     .map(state -> new UpdateBody(bodyId, state));
    }

}
