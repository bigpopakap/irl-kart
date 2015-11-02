package irl.fw.engine.collisions;

import irl.fw.shared.events.AddBody;
import irl.fw.shared.events.Collision;
import irl.fw.shared.events.RemoveBody;
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
