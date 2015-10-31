package irl.fw.physics.world;

import irl.fw.physics.bodies.IRLBody;
import irl.fw.physics.bodies.VirtualBody;
import irl.fw.physics.collisions.CollisionResolver;
import irl.fw.physics.events.Event;
import irl.fw.physics.runner.Loopable;
import irl.util.universe.Universe;
import rx.Observable;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 10/29/15
 */
public class World implements Loopable {

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
        eventQueue = eventQueue.mergeWith(body.updates());
        return universe.add(bodyInstance);
    }

    public String addVirtualBody(VirtualBody body) {
        return universe.add(new BodyInstance(body));
    }


    @Override
    public Observable<Event> eventQueue() {
        return eventQueue;
    }

    @Override
    public void onNext(Event event) {
        System.out.println("Processing event: " + event);
    }

    @Override
    public void render(long timeSinceLastUpdate) {
        System.out.println("Rendering");
    }

    @Override
    public void onCompleted() {
        //do nothing
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
    }

}