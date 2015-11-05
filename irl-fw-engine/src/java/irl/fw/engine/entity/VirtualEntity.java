package irl.fw.engine.entity;

import irl.fw.engine.physics.EntityState;
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
        return Observable.never();
    }

}
