package irl.kart.beacon;

import irl.util.concurrent.Looper;
import rx.Observable;
import rx.subjects.AsyncSubject;
import rx.subjects.PublishSubject;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/1/15
 */
public class AsyncRandomKartBeacon extends Looper implements KartBeacon {

    private final String[] kartIds;
    private volatile PublishSubject<KartUpdate> positions;
    private volatile int iteration = 0;

    public AsyncRandomKartBeacon(String... kartIds) {
        this.kartIds = kartIds;
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

        for (String kartId : kartIds) {
            KartUpdate update = new KartUpdate(kartId, kartId + "-pos-" + iteration++);

            //TODO remove
            System.out.println("Publishing update " + update);

            positions.onNext(update);
        }
    }

}
