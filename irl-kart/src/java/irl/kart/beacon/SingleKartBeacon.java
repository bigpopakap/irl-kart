package irl.kart.beacon;

import irl.kart.events.kart.KartEvent;
import irl.util.string.CompareUtils;
import rx.Observable;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/15/15
 */
public class SingleKartBeacon implements KartBeacon {

    private final String kartId;
    private final KartBeacon innerBeacon;

    public SingleKartBeacon(String kartId, KartBeacon beacon) {
        this.kartId = kartId;
        this.innerBeacon = beacon;
    }

    @Override
    public Observable<KartBeaconEvent> stream() {
        return innerBeacon.stream()
                    .filter(event -> CompareUtils.equal(kartId, event.getKartId()));
    }

    @Override
    public void send(KartEvent event) {
        if (CompareUtils.equal(kartId, event.getKartId())) {
            innerBeacon.send(event);
        }
    }

}
