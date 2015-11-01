package irl.fw.physics.world;

import irl.fw.physics.bodies.Body;
import irl.fw.physics.collisions.CollisionResolver;
import irl.fw.physics.collisions.NoopCollisionResolver;
import irl.fw.physics.events.*;
import irl.fw.physics.runner.EventQueueLoopable;
import irl.util.universe.Universe;

import java.util.concurrent.TimeUnit;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 10/29/15
 */
public class World extends EventQueueLoopable<PhysicalEvent> {

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

        String prepend = wasHandled ? "Handled" : "Unhandled or unexpected";
        System.out.println(prepend + " event type: " + event.getName());
    }

    void addBody(AddBody event) {
        Body bodyToAdd = event.getBody();
        BodyInstance instance = new BodyInstance(bodyToAdd);

        getEventQueue().merge(bodyToAdd.updates());

        String newBodyId = universe.add(instance);
        event.afterAddBody(newBodyId);
    }

    private void removeBody(RemoveBody event) {
        String bodyToRemove = event.getBodyId();
        if (universe.contains(bodyToRemove)) {
            universe.remove(bodyToRemove);
            event.afterRemoveBody(true);
        } else {
            System.out.println("Tried to remove non-existent body: " + bodyToRemove);
            event.afterRemoveBody(false);
        }
    }

    private void updateBody(UpdateBody event) {
        String bodyToUpdate = event.getBodyId();
        PhysicalState newState = event.getNewState();

        if (universe.contains(bodyToUpdate)) {
            universe.get(bodyToUpdate).setState(newState);
            event.afterUpdateBody(true);
        } else {
            System.out.println("Tried to update non-existent body: " + bodyToUpdate
                                + " to new state: " + newState);
            event.afterUpdateBody(false);
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