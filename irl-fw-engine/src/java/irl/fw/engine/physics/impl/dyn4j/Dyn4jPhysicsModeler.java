package irl.fw.engine.physics.impl.dyn4j;

import irl.fw.engine.entity.Entity;
import irl.fw.engine.entity.EntityInstance;
import irl.fw.engine.entity.state.EntityState;
import irl.fw.engine.collisions.CollisionResolver;
import irl.fw.engine.entity.state.EntityStateBuilder;
import irl.fw.engine.entity.state.EntityStateUpdate;
import irl.fw.engine.events.AddEntity;
import irl.fw.engine.events.RemoveEntity;
import irl.fw.engine.events.UpdateEntity;
import irl.fw.engine.physics.PhysicsModeler;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.World;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static irl.fw.engine.physics.impl.dyn4j.Dyn4jConverter.*;
import static irl.fw.engine.physics.impl.dyn4j.Dyn4jShapeConverter.*;

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
        world.setGravity(World.ZERO_GRAVITY);

        BodyFixture fixture = new BodyFixture(Geometry.createRectangle(0.2, 100));
        fixture.setRestitution(1.0);
        Body wallr = new Body();
        wallr.addFixture(fixture);
        wallr.translate(10.1, 0);
        wallr.setMass(MassType.INFINITE);
        world.addBody(wallr);

        world.setUpdateRequired(true);
    }

    @Override
    public Collection<EntityInstance> getEntities() {
        return world.getBodies().parallelStream()
                    .map(this::bodyToEntity)
                    .collect(Collectors.toList());
    }

    @Override
    public synchronized String add(AddEntity add) {
        Entity newEntity = add.getEntity();

        Body body = new Body();
        //FIXME need to set size, shape, mass, velocity and stuff
        body.setUserData(newEntity);

        //TODO remove these lines
        BodyFixture fixture = new BodyFixture(Geometry.createRectangle(0.5, 0.5));
        fixture.setRestitution(1.0);
        body.translate(5, 5);
        body.setMass(MassType.NORMAL);
        body.addFixture(fixture);
        body.setLinearVelocity(1, 1);

//        if (body.getFixtureCount() != 1) {
//            throw new IllegalStateException("We need exactly one fixture per body");
//        }
        body.setActive(true);
        body.setAsleep(false);

        world.addBody(body);
        world.setUpdateRequired(true);

        return body.getId().toString();
    }

    @Override
    public synchronized void remove(RemoveEntity remove) {
        String entityId = remove.getEntityId();
        Optional<Body> foundBody = findBody(entityId);

        if (foundBody.isPresent()) {
            world.removeBody(foundBody.get());
            world.setUpdateRequired(true);
        } else {
            System.err.println("Tried to remove non-existent body: " + entityId);
        }
    }

    @Override
    public synchronized void update(UpdateEntity update) {
        String entityId = update.getEntityId();
        EntityStateUpdate stateUpdate = update.getStateUpdate();

        Optional<Body> foundBody = findBody(entityId);

        if (foundBody.isPresent()) {
            Body body = foundBody.get();

            EntityInstance current = bodyToEntity(body);
            EntityState newState = stateUpdate.fillAndBuild(current.getState());

            body.translateToOrigin();
            body.translate(fromVector(newState.getCenter()));
            body.setLinearVelocity(fromVector(newState.getVelocity()));

            world.setUpdateRequired(true);
        } else {
            System.err.println("Tried to update non-existent body: " + entityId);
        }
    }

    @Override
    public synchronized void model(CollisionResolver collisionResolver, long timeStep) {
        double timeStepInSeconds = timeStep / 1000.0;
        world.update(timeStepInSeconds);
    }

    private Optional<Body> findBody(String entityId) {
        return world.getBodies().stream()
                .filter(body -> body.getId().equals(UUID.fromString(entityId)))
                .findFirst();
    }

    private EntityInstance bodyToEntity(Body body) {
        Entity entity = (Entity) body.getUserData();

        EntityState state = new EntityStateBuilder()
            .shape(toShape(body.getFixture(0).getShape()))
            .center(toVector(body.getWorldCenter()))
            .velocity(toVector(body.getLinearVelocity()))
            .build();

        return new EntityInstance(entity, state);
    }

}
