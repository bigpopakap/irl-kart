package irl.fw.engine.world;

import irl.fw.engine.bodies.PhysicalState;
import irl.fw.engine.events.AddBody;
import irl.fw.engine.events.PhysicalEvent;
import irl.fw.engine.events.RemoveBody;
import irl.fw.engine.events.UpdateBody;
import irl.fw.engine.bodies.Body;
import irl.fw.engine.collisions.CollisionResolver;
import irl.fw.engine.simulation.Simulatable;
import irl.util.reactiveio.Pipe;
import irl.util.universe.Universe;

import java.util.Map;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 10/29/15
 */
public class World implements Simulatable<PhysicalEvent> {

    private final CollisionResolver collisionResolver;
    private final Universe<BodyInstance> universe;
    private Pipe<PhysicalEvent> eventQueue;

    World(CollisionResolver collisionResolver) {
        if (collisionResolver == null) {
            throw new RuntimeException("These fields cannot be null");
        }

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
        } else {
            System.err.println("Tried to update non-existent body: " + bodyToUpdate
                    + " to new state: " + newState);
        }
    }

    @Override
    public void updatePhysics(long timeStep) {
        //TODO
    }

    @Override
    public void render(long timeSinceLastUpdate) {
        long now = System.currentTimeMillis();

        StringBuilder str = new StringBuilder();

        str.append("\nWorld")
            .append(String.format(" updated at %s", (now - timeSinceLastUpdate)))
            .append(String.format(", rendered %s millis later\n", timeSinceLastUpdate));

        for (Map.Entry<String, BodyInstance> idAndBody : universe) {
            String id = idAndBody.getKey();
            PhysicalState state = idAndBody.getValue().getState();

            str.append(String.format("Body %s in state %s\n", id, state));
        }

        System.out.print(str);
    }
}