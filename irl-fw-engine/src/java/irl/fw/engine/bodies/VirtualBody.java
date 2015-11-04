package irl.fw.engine.bodies;

import rx.Observable;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 10/29/15
 */
public interface VirtualBody extends Body {

    @Override
    default Observable<PhysicalState> updates() {
        return Observable.never();
    }

}
