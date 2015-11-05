package irl.fw.engine.physics.impl.dyn4j;

import irl.fw.engine.entity.Entity;
import irl.fw.engine.entity.EntityInstance;
import irl.fw.engine.physics.EntityState;
import irl.fw.engine.collisions.CollisionResolver;
import irl.fw.engine.events.AddEntity;
import irl.fw.engine.events.RemoveEntity;
import irl.fw.engine.events.UpdateEntity;
import irl.fw.engine.physics.PhysicsModeler;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.World;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Rectangle;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/5/15
 */
public class Dyn4jPhysicsModeler implements PhysicsModeler {

    private final World world;

    public Dyn4jPhysicsModeler() {
        world = new World();

        //add a floor
        Body floor = new Body();
        floor.addFixture(new Rectangle(100, 100));
        floor.setMass(MassType.INFINITE);
        floor.translate(-50, -50);
        world.addBody(floor);
    }

    @Override
    public Collection<EntityInstance> getEntities() {
        return world.getBodies().parallelStream()
                    .map(this::bodyToEntity)
                    .collect(Collectors.toList());
    }

    @Override
    public synchronized String add(AddEntity event) {
        Body body = new Body();
        //FIXME need to set size, shape, mass, velocity and stuff
        body.setUserData(event.getEntity());

        //TODO remove these lines
        body.translate(5, 5);
        body.setMass(MassType.NORMAL);
        body.addFixture(new Rectangle(3, 3));
        body.setLinearVelocity(20, 20);

//        if (body.getFixtureCount() != 1) {
//            throw new IllegalStateException("We need exactly one fixture per body");
//        }
        body.setActive(true);
        body.setAsleep(false);
        world.addBody(body);
        return body.getId().toString();
    }

    @Override
    public synchronized void remove(RemoveEntity event) {
        String id = event.getEntityId();
        Optional<Body> foundBody = findBody(id);

        if (foundBody.isPresent()) {
            world.removeBody(foundBody.get());
        } else {
            System.err.println("Tried to remove non-existent body: " + id);
        }
    }

    @Override
    public synchronized void update(UpdateEntity event) {
        String id = event.getEntityId();
        Optional<Body> foundBody = findBody(id);

        if (foundBody.isPresent()) {
            Body body = foundBody.get();
            body.translateToOrigin();
            body.translate(event.getNewState().getCenter());
        } else {
            System.err.println("Tried to update non-existent body: " + id);
        }
    }

    @Override
    public synchronized void model(CollisionResolver collisionResolver, long timeStep) {
        double timeStepInSeconds = timeStep / 1000.0;
        boolean updated = world.update(timeStepInSeconds);
    }

    private Optional<Body> findBody(String entityId) {
        return world.getBodies().stream()
                .filter(body -> body.getId().equals(UUID.fromString(entityId)))
                .findFirst();
    }

    private EntityInstance bodyToEntity(Body body) {
        Entity entity = (Entity) body.getUserData();

        EntityState state = new EntityState(
            body.getWorldCenter(),
            body.getLinearVelocity()
        );

        return new EntityInstance(entity, state);
    }

}
