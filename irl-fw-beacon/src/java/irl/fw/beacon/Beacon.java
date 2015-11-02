package irl.fw.beacon;

import rx.Observable;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/1/15
 */
public interface Beacon {

    Observable<BeaconUpdate> updates();

}
