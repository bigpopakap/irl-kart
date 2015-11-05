package irl.fw.engine.physics.impl.dyn4j;

import irl.fw.engine.bodies.Body;
import irl.fw.engine.bodies.BodyInstance;
import irl.fw.engine.bodies.PhysicalState;
import irl.fw.engine.collisions.CollisionResolver;
import irl.fw.engine.events.AddBody;
import irl.fw.engine.events.RemoveBody;
import irl.fw.engine.events.UpdateBody;
import irl.fw.engine.physics.PhysicsModeler;
import irl.util.universe.Universe;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/5/15
 */
public class Dyn4jPhysicsModeler implements PhysicsModeler {

    private final Universe<BodyInstance> universe;

    public Dyn4jPhysicsModeler() {
        universe = new Universe<>();
    }

    @Override
    public Universe<BodyInstance> getBodies() {
        return universe;
    }

    @Override
    public String addBody(AddBody event) {
        Body bodyToAdd = event.getBody();
        BodyInstance bodyInstance = new BodyInstance(
                bodyToAdd,
                event.getInitialState()
        );
        return universe.add(bodyInstance);
    }

    @Override
    public BodyInstance removeBody(RemoveBody event) {
        String bodyToRemove = event.getBodyId();
        if (universe.contains(bodyToRemove)) {
            return universe.remove(bodyToRemove);
        } else {
            System.err.println("Tried to remove non-existent body: " + bodyToRemove);
            return null;
        }
    }

    @Override
    public void updateBody(UpdateBody event) {
        String bodyToUpdate = event.getBodyId();
        PhysicalState newState = event.getNewState();

        if (universe.contains(bodyToUpdate)) {
            BodyInstance current = universe.get(bodyToUpdate);
            BodyInstance updated = current.setState(newState);
            universe.update(bodyToUpdate, updated);
        } else {
            System.err.println("Tried to update non-existent body: " + bodyToUpdate
                    + " to new state: " + newState);
        }
    }

    @Override
    public void model(CollisionResolver collisionResolver, long timeStep) {
        //TODO
    }

}
