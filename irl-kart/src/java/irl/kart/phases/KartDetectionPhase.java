package irl.kart.phases;

import irl.fw.engine.entity.state.EntityStateBuilder;
import irl.fw.engine.events.AddEntity;
import irl.fw.engine.events.EngineEvent;
import irl.fw.engine.geometry.Angle;
import irl.fw.engine.geometry.Vector2D;
import irl.kart.beacon.KartBeacon;
import irl.kart.beacon.KartBeaconEvent;
import irl.kart.entities.Kart;
import irl.kart.events.beacon.KartStateUpdate;
import irl.util.concurrent.SynchronousRunnable;
import irl.util.reactiveio.EventQueue;
import rx.Observable;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/15/15
 */
public class KartDetectionPhase extends SynchronousRunnable {

    private final EventQueue<EngineEvent> eventQueue;
    private final KartBeacon beacon;

    public KartDetectionPhase(EventQueue<EngineEvent> eventQueue, KartBeacon beacon) {
        this.eventQueue = eventQueue;
        this.beacon = beacon;
    }

    @Override
    protected void doRunSynchronous() {
        eventQueue.mergeIn(addNewKarts());
    }

    private Observable<AddEntity> addNewKarts() {
        return beacon.stream()
                .ofType(KartStateUpdate.class)
                .distinct(KartBeaconEvent::getKartId)
                .map(update -> new AddEntity(entityConfig -> new Kart(
                        entityConfig,
                        new EntityStateBuilder()
                                .shape(Kart.SHAPE)
                                .rotation(Angle.deg(0))
                                .center(new Vector2D(200, 200))
                                .velocity(new Vector2D(0, 0))
                                .angularVelocity(Angle.deg(0))
                                .build(),
                        update.getKartId(), beacon, eventQueue
                )));
    }

}
