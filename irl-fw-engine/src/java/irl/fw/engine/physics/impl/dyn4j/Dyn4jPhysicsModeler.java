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
import org.dyn4j.geometry.Rectangle;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
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
        //TODO add floor here?
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
        body.addFixture(new Rectangle(3, 3)); //TODO remove

        if (body.getFixtureCount() != 1) {
            throw new IllegalStateException("We need exactly one fixture per body");
        }
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
            //FIXME figure out how to translate this thing
        } else {
            System.err.println("Tried to update non-existent body: " + id);
        }
    }

    @Override
    public synchronized void model(CollisionResolver collisionResolver, long timeStep) {
        double timeStepInSeconds = TimeUnit.SECONDS.convert(timeStep, TimeUnit.MILLISECONDS);
        world.update(timeStepInSeconds);
    }

    private Optional<Body> findBody(String entityId) {
        return world.getBodies().stream()
                .filter(body -> body.getId().equals(UUID.fromString(entityId)))
                .findFirst();
    }

    private EntityInstance bodyToEntity(Body body) {
        Entity entity = (Entity) body.getUserData();

        EntityState state = new EntityState(
            body.getFixture(0).getShape(),
            body.getLinearVelocity()
        );

        return new EntityInstance(entity, state);
    }

}
