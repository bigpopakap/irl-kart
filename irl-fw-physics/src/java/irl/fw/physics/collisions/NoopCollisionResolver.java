package irl.fw.physics.collisions;

import irl.fw.physics.events.AddBody;
import irl.fw.physics.events.Collision;
import irl.fw.physics.events.RemoveBody;
import rx.Observable;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/1/15
 */
public class NoopCollisionResolver implements CollisionResolver {

    @Override
    public void onCollision(Collision collision) {
        //do nothing
    }

    @Override
    public Observable<AddBody> adds() {
        return Observable.empty();
    }

    @Override
    public Observable<RemoveBody> removes() {
        return Observable.empty();
    }

}
