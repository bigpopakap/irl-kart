package irl.fw.physics.collisions;

import irl.fw.physics.events.AddBody;
import irl.fw.physics.events.Collision;
import irl.fw.physics.events.RemoveBody;
import rx.Observable;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 10/29/15
 */
public interface CollisionResolver {

    void onCollision(Collision collision);

    Observable<AddBody> getAdds();
    Observable<RemoveBody> getRemoves();

}
