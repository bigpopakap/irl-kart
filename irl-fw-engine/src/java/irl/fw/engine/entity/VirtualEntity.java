package irl.fw.engine.entity;

import irl.fw.engine.entity.state.EntityStateUpdate;
import rx.Observable;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 10/29/15
 */
public abstract class VirtualEntity implements Entity {

    @Override
    public final boolean isVirtual() {
        return true;
    }

    @Override
    public final Observable<EntityStateUpdate> updates() {
        return Observable.empty();
    }

}
