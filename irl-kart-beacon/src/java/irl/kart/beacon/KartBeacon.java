package irl.kart.beacon;

import rx.Observable;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/1/15
 */
//FIXME should this be genericized and put into a irl-fw-beacon module?
public interface KartBeacon {

    Observable<KartUpdate> updates();

}
