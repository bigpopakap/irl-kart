package irl.kart.bodies;

import irl.fw.engine.beacon.Beacon;
import irl.fw.engine.bodies.IRLBody;
import irl.fw.engine.bodies.PhysicalState;
import irl.util.string.StringUtils;
import rx.Observable;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/1/15
 */
public class TestBody implements IRLBody {

    private final String kartId;
    private final Beacon beacon;

    public TestBody(String kartId, Beacon beacon) {
        this.kartId = kartId;
        this.beacon = beacon;
    }

    @Override
    public Observable<PhysicalState> updates() {
        //TODO we should only report the latest position or something
        return beacon.updates()
                    .filter(update -> StringUtils.equal(kartId, update.getExternalId()))
                    .map(update -> update.getState());
    }

}
