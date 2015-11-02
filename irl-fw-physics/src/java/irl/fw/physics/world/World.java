package irl.fw.physics.world;

import irl.fw.physics.bodies.Body;
import irl.fw.physics.collisions.CollisionResolver;
import irl.fw.physics.collisions.NoopCollisionResolver;
import irl.fw.physics.events.*;
import irl.fw.physics.runner.EventQueueSimulatable;
import irl.util.universe.Universe;

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
        queue(this.collisionResolver.adds(), this.collisionResolver.removes());
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
        if (event instanceof AddBody) {
            addBody((AddBody) event);
        } else if (event instanceof RemoveBody) {
            removeBody((RemoveBody) event);
        } else if (event instanceof UpdateBody) {
            updateBody((UpdateBody) event);
        } else {
            System.err.println("Unhandled or unexpected event: " + event.getName());
        }
    }

    void addBody(AddBody event) {
        Body bodyToAdd = event.getBody();
        BodyInstance bodyInstance = new BodyInstance(bodyToAdd);

        String newBodyId = universe.add(bodyInstance);

        queue(bodyToAdd.updates(newBodyId));
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
    public void updatePhysics() {
        //TODO updatePhysics physics
    }

    @Override
    public void render(long timeSinceLastUpdate) {
        //TODO send render events
    }
}