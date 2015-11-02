package irl.fw.physics.world;

import irl.fw.physics.bodies.Body;
import irl.fw.physics.collisions.CollisionResolver;
import irl.fw.physics.collisions.NoopCollisionResolver;
import irl.fw.physics.events.*;
import irl.fw.physics.runner.EventQueueSimulatable;
import irl.util.universe.Universe;

import java.util.concurrent.TimeUnit;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 10/29/15
 */
public class World extends EventQueueSimulatable<PhysicalEvent> {

    private final CollisionResolver collisionResolver;
    private final Universe<BodyInstance> universe;

    public World() {
        this(new NoopCollisionResolver());
    }

    public World(CollisionResolver collisionResolver) {
        universe = new Universe<>();

        this.collisionResolver = collisionResolver;
        getEventQueue().merge(this.collisionResolver.adds())
                        .merge(this.collisionResolver.removes());
    }

    @Override
    public void onCompleted() {
        //do nothing
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace(System.out);
    }

    @Override
    public void onNext(PhysicalEvent event) {
        //TODO should each event implement a do() method?
        boolean wasHandled = true;
        if (event instanceof AddBody) {
            addBody((AddBody) event);
        } else if (event instanceof RemoveBody) {
            removeBody((RemoveBody) event);
        } else if (event instanceof UpdateBody) {
            updateBody((UpdateBody) event);
        } else {
            wasHandled = false;
        }
    }

    void addBody(AddBody event) {
        Body bodyToAdd = event.getBody();
        BodyInstance bodyInstance = new BodyInstance(bodyToAdd);

        String newBodyId = universe.add(bodyInstance);

        getEventQueue().merge(bodyToAdd.updates(newBodyId));
    }

    private void removeBody(RemoveBody event) {
        String bodyToRemove = event.getBodyId();
        if (universe.contains(bodyToRemove)) {
            universe.remove(bodyToRemove);
        } else {
            System.err.println("Tried to remove non-existent body: " + bodyToRemove);
        }
    }

    private void updateBody(UpdateBody event) {
        String bodyToUpdate = event.getBodyId();
        PhysicalState newState = event.getNewState();

        if (universe.contains(bodyToUpdate)) {
            universe.get(bodyToUpdate).setState(newState);
            //TODO remove
            System.out.println("Updated body " + bodyToUpdate + " to state " + newState);
        } else {
            System.err.println("Tried to update non-existent body: " + bodyToUpdate
                    + " to new state: " + newState);
        }
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