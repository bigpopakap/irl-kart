package irl.fw.physics.world;

import irl.fw.physics.bodies.IRLBody;
import irl.fw.physics.bodies.VirtualBody;
import irl.fw.physics.collisions.CollisionResolver;
import irl.fw.physics.events.PhysicalEvent;
import irl.fw.physics.runner.EventQueueLooper;
import irl.util.universe.Universe;

import java.util.concurrent.TimeUnit;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 10/29/15
 */
public class World extends EventQueueLooper<PhysicalEvent> {

    private final CollisionResolver collisionResolver;
    private final Universe<BodyInstance> universe;

    public World(CollisionResolver collisionResolver) {
        universe = new Universe<>();

        this.collisionResolver = collisionResolver;
        getEventQueue().merge(this.collisionResolver.adds())
                        .merge(this.collisionResolver.removes());
    }

    public String addIRLBody(IRLBody body) {
        BodyInstance bodyInstance = new BodyInstance(body);
        getEventQueue().merge(body.updates());
        return universe.add(bodyInstance);
    }

    public String addVirtualBody(VirtualBody body) {
        return universe.add(new BodyInstance(body));
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(PhysicalEvent physicalEvent) {

    }

    @Override
    public void updatePhysics(long timeStep, TimeUnit timeUnit) {
        //TODO updatePhysics physics
    }

    @Override
    public void render(long timeSinceLastUpdate, TimeUnit timeUnit) {
        //TODO send render events
    }
}