package irl.fw.engine.world;

import irl.fw.shared.bodies.PhysicalState;
import irl.fw.shared.events.AddBody;
import irl.fw.shared.events.PhysicalEvent;
import irl.fw.shared.events.RemoveBody;
import irl.fw.shared.events.UpdateBody;
import irl.fw.shared.bodies.Body;
import irl.fw.engine.collisions.CollisionResolver;
import irl.fw.physics.modeling.PhysicsModeler;
import irl.fw.engine.runner.Simulatable;
import irl.util.reactiveio.Pipe;
import irl.util.universe.Universe;

import java.util.concurrent.TimeUnit;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 10/29/15
 */
public class World implements Simulatable<PhysicalEvent> {

    private final PhysicsModeler physicsModeler;
    private final CollisionResolver collisionResolver;
    private final Universe<BodyInstance> universe;
    private Pipe<PhysicalEvent> eventQueue;

    World(PhysicsModeler physicsModeler, CollisionResolver collisionResolver) {
        if (physicsModeler == null || collisionResolver == null) {
            throw new RuntimeException("These fields cannot be null");
        }

        this.physicsModeler = physicsModeler;
        this.collisionResolver = collisionResolver;
        universe = new Universe<>();
    }

    @Override
    public void start(Pipe<PhysicalEvent> eventQueue) {
        this.eventQueue = eventQueue;
        this.eventQueue.mergeIn(this.collisionResolver.adds());
        this.eventQueue.mergeIn(this.collisionResolver.removes());
    }

    @Override
    public void handleEvent(PhysicalEvent event) {
        //TODO use command pattern instead of hardcoding a method per event
        if (event instanceof AddBody) {
            handleAddBody((AddBody) event);
        } else if (event instanceof RemoveBody) {
            handleRemoveBody((RemoveBody) event);
        } else if (event instanceof UpdateBody) {
            handleUpdateBody((UpdateBody) event);
        } else {
            System.err.println("Unhandled or unexpected event: " + event.getName());
        }
    }

    private void handleAddBody(AddBody event) {
        Body bodyToAdd = event.getBody();
        BodyInstance bodyInstance = new BodyInstance(
            bodyToAdd,
            event.getInitialState()
        );

        String newBodyId = universe.add(bodyInstance);

        eventQueue.mergeIn(bodyToAdd.updates(newBodyId));

        System.out.println("Added body " + newBodyId + " with intial state " + bodyInstance.getState());
    }

    private void handleRemoveBody(RemoveBody event) {
        String bodyToRemove = event.getBodyId();
        if (universe.contains(bodyToRemove)) {
            universe.remove(bodyToRemove);
        } else {
            System.err.println("Tried to remove non-existent body: " + bodyToRemove);
        }
    }

    private void handleUpdateBody(UpdateBody event) {
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
    public void updatePhysics(long timeStep) {
        physicsModeler.runModel(timeStep);
    }

    @Override
    public void render(long timeSinceLastUpdate, TimeUnit timeUnit) {
        //TODO send render events
    }
}