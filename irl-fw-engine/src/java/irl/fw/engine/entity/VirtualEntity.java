package irl.fw.engine.entity;

import irl.fw.engine.entity.state.EntityState;
import rx.Observable;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 10/29/15
 */
public interface VirtualEntity extends Entity {

    @Override
    default Observable<EntityState> updates() {
        return Observable.empty();
    }

}
