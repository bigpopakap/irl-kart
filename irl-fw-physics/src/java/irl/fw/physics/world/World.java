package irl.fw.physics.world;

import irl.fw.physics.bodies.IRLBody;
import irl.fw.physics.bodies.VirtualBody;
import irl.fw.physics.collisions.CollisionResolver;
import irl.fw.physics.events.PhysicalEvent;
import irl.fw.physics.runner.Loopable;
import irl.util.universe.Universe;
import rx.Observable;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 10/29/15
 */
public class World implements Loopable<PhysicalEvent> {

    private final CollisionResolver collisionResolver;
    private final Universe<BodyInstance> universe;

    private final EventQueue<PhysicalEvent> eventQueue;

    public World(CollisionResolver collisionResolver) {
        universe = new Universe<>();
        eventQueue = new EventQueue<>();

        this.collisionResolver = collisionResolver;
        eventQueue.merge(this.collisionResolver.adds())
                  .merge(this.collisionResolver.removes());
    }

    public String addIRLBody(IRLBody body) {
        BodyInstance bodyInstance = new BodyInstance(body);
        eventQueue.merge(body.updates());
        return universe.add(bodyInstance);
    }

    public String addVirtualBody(VirtualBody body) {
        return universe.add(new BodyInstance(body));
    }

    @Override
    public Observable<PhysicalEvent> eventQueue() {
        return eventQueue.getQueue();
    }

    @Override
    public void onNext(PhysicalEvent event) {
        System.out.println("Processing event: " + event.getName());
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