package irl.kart.beacon.impl;

import irl.fw.engine.geometry.Vector2D;
import irl.kart.beacon.KartBeacon;
import irl.kart.beacon.KartUpdate;
import irl.util.concurrent.LoopingRunnable;
import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/1/15
 */
public class AsyncRandomKartBeacon extends LoopingRunnable implements KartBeacon {

    private final String[] externalIds;
    private final Subject<KartUpdate, KartUpdate> positions;
    private volatile int iteration = 0;

    public AsyncRandomKartBeacon(String... externalIds) {
        this.externalIds = externalIds;
        positions = PublishSubject.<KartUpdate>create().toSerialized();
    }

    public Observable<KartUpdate> updates() {
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
            KartUpdate update = new KartUpdate(
                externalId,
                new Vector2D(iteration, iteration),
                new Vector2D(iteration, iteration)
            );
            iteration++;

            positions.onNext(update);
        }
    }

}
