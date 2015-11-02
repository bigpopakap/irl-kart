package irl.fw.engine.collisions;

import irl.fw.shared.events.Collision;
import irl.fw.shared.events.AddBody;
import irl.fw.shared.events.RemoveBody;
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
        return Observable.never();
    }

    @Override
    public Observable<RemoveBody> removes() {
        return Observable.never();
    }

}
