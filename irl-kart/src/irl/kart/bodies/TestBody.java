package irl.kart.bodies;

import irl.fw.physics.bodies.IRLBody;
import irl.fw.physics.events.UpdateBody;
import irl.fw.physics.world.PhysicalState;
import irl.kart.beacon.HardcodedKartBeacon;
import irl.kart.beacon.KartBeacon;
import rx.Observable;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/1/15
 */
public class TestBody implements IRLBody {

    private KartBeacon beacon;

    public TestBody(KartBeacon beacon) {
        this.beacon = beacon;
    }

    @Override
    public Observable<UpdateBody> updates(String bodyId) {
        //TODO we should only report the latest position or something
        //TODO filter by the kart's ID somehow
        return beacon.updates()
                     .map((update) -> new PhysicalState(update.getPosition()))
                     .map((state) -> new UpdateBody(bodyId, state));
    }

}
