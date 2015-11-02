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
import irl.util.universe.Universe;
import rx.Observable;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 10/29/15
 */
public class World implements Simulatable<PhysicalEvent> {

    private Consumer<Observable<? extends PhysicalEvent>> queueEvents;
    private final PhysicsModeler physicsModeler;
    private final CollisionResolver collisionResolver;
    private final Universe<BodyInstance> universe;

    World(PhysicsModeler physicsModeler, CollisionResolver collisionResolver) {
        if (physicsModeler == null || collisionResolver == null) {
            throw new RuntimeException("These fields cannot be null");
        }

        this.physicsModeler = physicsModeler;
        this.collisionResolver = collisionResolver;
        universe = new Universe<>();
    }

    @Override
    public void start(Consumer<Observable<? extends PhysicalEvent>> queueEvents) {
        this.queueEvents = queueEvents;
        this.queueEvents.accept(this.collisionResolver.adds());
        this.queueEvents.accept(this.collisionResolver.removes());
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
        BodyInstance bodyInstance = new BodyInstance(bodyToAdd);

        String newBodyId = universe.add(bodyInstance);

        queueEvents.accept(bodyToAdd.updates(newBodyId));
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