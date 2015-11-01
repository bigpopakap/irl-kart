package irl.kart.bodies;

import irl.fw.physics.bodies.IRLBody;
import irl.fw.physics.events.PhysicalEvent;
import irl.fw.physics.events.UpdateBody;
import irl.fw.physics.world.PhysicalState;
import irl.util.events.EventQueue;
import rx.Observable;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/1/15
 */
public class TestBody implements IRLBody {

    private EventQueue<PhysicalState> positions;

    public TestBody() {
        positions = new EventQueue<>();

        //TODO this is where the live data will come
        positions.merge(Observable.from(new PhysicalState[] {
            new PhysicalState(),
            new PhysicalState(),
            new PhysicalState()
        }));
    }

    @Override
    public Observable<UpdateBody> updates(String bodyId) {
        //TODO we should only report the latest position or something
        return positions.getQueue()
                        .map((state) -> new UpdateBody(bodyId, state));
    }

}
