package irl.kart.beacon;

import irl.kart.events.kart.KartEvent;
import rx.Observable;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/1/15
 */
public interface KartBeacon {

    Observable<KartBeaconEvent> stream();
    void send(KartEvent event);

}
