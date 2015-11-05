package irl.fw.engine.beacon.impl;

import irl.fw.engine.beacon.Beacon;
import irl.fw.engine.beacon.BeaconUpdate;
import irl.fw.engine.physics.EntityState;
import irl.util.concurrent.LoopingRunnable;
import org.dyn4j.geometry.Vector2;
import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/1/15
 */
public class AsyncRandomBeacon extends LoopingRunnable implements Beacon {

    private final String[] externalIds;
    private final Subject<BeaconUpdate, BeaconUpdate> positions;
    private volatile int iteration = 0;

    public AsyncRandomBeacon(String... externalIds) {
        this.externalIds = externalIds;
        positions = PublishSubject.<BeaconUpdate>create().toSerialized();
    }

    public Observable<BeaconUpdate> updates() {
        return positions;
    }

    @Override
    protected void loopIteration() {
        try {
            Thread.sleep((long) (Math.random() * 1000));
        } catch (InterruptedException e) {
            //do nothing
        }

        for (String externalId : externalIds) {
            BeaconUpdate update = new BeaconUpdate(
                externalId,
                new EntityState(null, new Vector2(iteration, iteration))
            );
            iteration++;

            positions.onNext(update);
        }
    }

}
