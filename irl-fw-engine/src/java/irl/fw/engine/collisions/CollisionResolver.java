package irl.fw.engine.collisions;

import irl.fw.engine.events.AddBody;
import irl.fw.engine.events.Collision;
import irl.fw.engine.events.RemoveBody;
import rx.Observable;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 10/29/15
 */
public interface CollisionResolver {

    void onCollision(Collision collision);

    Observable<AddBody> adds();
    Observable<RemoveBody> removes();

}
