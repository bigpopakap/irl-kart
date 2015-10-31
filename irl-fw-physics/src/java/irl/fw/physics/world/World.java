package irl.fw.physics.world;

import irl.fw.physics.bodies.IRLBody;
import irl.fw.physics.bodies.VirtualBody;
import irl.fw.physics.collisions.CollisionResolver;
import irl.fw.physics.events.Collision;
import irl.fw.physics.events.Event;
import irl.fw.physics.events.UpdateBody;
import irl.util.universe.Universe;
import rx.Observable;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 10/29/15
 */
public class World {

    private final CollisionResolver collisionResolver;
    private final Universe<BodyInstance> universe;
    private volatile Observable<Event> eventQueue;

    public World(CollisionResolver collisionResolver) {
        universe = new Universe<>();
        eventQueue = Observable.empty();

        this.collisionResolver = collisionResolver;
        eventQueue = eventQueue.mergeWith(this.collisionResolver.getAdds())
                                .mergeWith(this.collisionResolver.getRemoves());
    }

    public String addIRLBody(IRLBody body) {
        BodyInstance bodyInstance = new BodyInstance(body);
        eventQueue = eventQueue.mergeWith(body.getUpdates());
        return universe.add(bodyInstance);
    }

    public String addVirtualBody(VirtualBody body) {
        return universe.add(new BodyInstance(body));
    }

    public Observable<UpdateBody> getUpdates() {
        return Observable.empty();
    }

    public Observable<Collision> getCollisions() {
        return Observable.empty();
    }

}