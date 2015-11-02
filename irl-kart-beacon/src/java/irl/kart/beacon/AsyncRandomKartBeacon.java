package irl.kart.beacon;

import irl.fw.beacon.Beacon;
import irl.fw.beacon.BeaconUpdate;
import irl.fw.shared.bodies.PhysicalState;
import irl.util.concurrent.Looper;
import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/1/15
 */
public class AsyncRandomKartBeacon extends Looper implements Beacon {

    private final String[] kartIds;
    private volatile PublishSubject<BeaconUpdate> positions;
    private volatile int iteration = 0;

    public AsyncRandomKartBeacon(String... kartIds) {
        this.kartIds = kartIds;
        positions = PublishSubject.create();
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

        for (String kartId : kartIds) {
            BeaconUpdate update = new BeaconUpdate(
                kartId,
                new PhysicalState(kartId + "-pos-" + iteration++)
            );

            //TODO remove
            System.out.println("Publishing update " + update);

            positions.onNext(update);
        }
    }

}
