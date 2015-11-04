package irl.fw.engine.bodies;

import irl.fw.engine.events.UpdateBody;
import rx.Observable;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 10/29/15
 */
public interface VirtualBody extends Body {

    @Override
    default Observable<UpdateBody> updates(String bodyId) {
        return Observable.never();
    }

}
