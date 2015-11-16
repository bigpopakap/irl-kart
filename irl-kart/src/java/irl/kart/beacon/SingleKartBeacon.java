package irl.kart.beacon;

import irl.kart.events.kart.KartEvent;
import irl.util.string.StringUtils;
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

    public SingleKartBeacon(String kartId, KartBeacon innerBeacon) {
        this.kartId = kartId;
        this.innerBeacon = innerBeacon;
    }

    @Override
    public Observable<KartBeaconEvent> stream() {
        return innerBeacon.stream()
                .filter(update -> StringUtils.equal(kartId, update.getKartId()));
    }

    @Override
    public void send(KartEvent event) {
        innerBeacon.send(event);
    }

}
