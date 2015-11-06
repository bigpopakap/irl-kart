package irl.fw.engine.entity;

import irl.fw.engine.entity.state.EntityStateUpdate;
import rx.Observable;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 10/29/15
 */
public interface Entity {

    Observable<EntityStateUpdate> updates();

}
