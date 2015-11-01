package irl.kart.beacon;

import irl.util.loop.Loop;
import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/1/15
 */
//TODO should this be genericized and put into a irl-fw-beacon module?
public class KartBeacon extends Loop {

    private volatile PublishSubject<KartUpdate> positions;

    public KartBeacon() {
        positions = PublishSubject.create();
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

        KartUpdate update = new KartUpdate("my cart", "update-" + System.currentTimeMillis());
        System.out.println("Publishing update " + update);
        positions.onNext(update);
    }
}
