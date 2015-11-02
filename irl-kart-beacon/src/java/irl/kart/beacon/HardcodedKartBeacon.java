package irl.kart.beacon;

import irl.util.loop.SimpleLooper;
import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/1/15
 */
public class HardcodedKartBeacon extends SimpleLooper implements KartBeacon {

    private volatile PublishSubject<KartUpdate> positions;
    private volatile int iteration = 0;

    public HardcodedKartBeacon() {
        positions = PublishSubject.create();
    }

    @Override
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

        KartUpdate update = new KartUpdate("my cart", "update-" + iteration++);
        //TODO remove
        System.out.println("Publishing update " + update);
        positions.onNext(update);
    }

}
