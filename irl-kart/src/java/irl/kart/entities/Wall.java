package irl.kart.entities;

import irl.fw.engine.entity.IRLEntity;
import irl.fw.engine.entity.state.EntityStateUpdate;
import rx.Observable;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/10/15
 */
public class Wall extends IRLEntity {

    @Override
    public Observable<EntityStateUpdate> updates() {
        //walls don't change positions
        return Observable.empty();
    }

}
