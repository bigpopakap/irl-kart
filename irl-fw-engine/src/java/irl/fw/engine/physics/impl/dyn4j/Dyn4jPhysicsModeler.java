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
import irl.fw.engine.world.SimpleWorld;
import org.dyn4j.collision.AbstractCollidable;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.World;
import org.dyn4j.geometry.*;

import java.util.*;
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
    }

    @Override
    public irl.fw.engine.world.World getWorld() {
        Set<EntityInstance> entityInstances
            = world.getBodies().parallelStream()
                .map(this::bodyToEntity)
                .collect(Collectors.toSet());

        Set<AABB> allBounds = world.getBodies().parallelStream()
                        .map(AbstractCollidable::createAABB)
                        .collect(Collectors.toSet());

        double minX = allBounds.parallelStream()
                .map(AABB::getMinX)
                .min(Double::compare)
                .orElse(0.0);

        double maxX = allBounds.parallelStream()
                .map(AABB::getMaxX)
                .max(Double::compare)
                .orElse(0.0);

        double minY = allBounds.parallelStream()
                .map(AABB::getMinY)
                .min(Double::compare)
                .orElse(0.0);

        double maxY = allBounds.parallelStream()
                .map(AABB::getMaxY)
                .max(Double::compare)
                .orElse(0.0);

        return new SimpleWorld(entityInstances, minX, maxX, minY, maxY);
    }

    @Override
    public synchronized String addEntity(AddEntity add) {
        Body body = createBody(add.getEntity(), add.getInitialState());

        //addEntity the body
        world.addBody(body);
        world.setUpdateRequired(true);

        return body.getId().toString();
    }

    @Override
    public synchronized void removeEntity(RemoveEntity remove) {
        String entityId = remove.getEntityId();
        Optional<Body> foundBody = findBody(entityId);

        if (foundBody.isPresent()) {
            world.removeBody(foundBody.get());
            world.setUpdateRequired(true);
        } else {
            System.err.println("Tried to removeEntity non-existent body: " + entityId);
        }
    }

    @Override
    public synchronized void updateEntity(UpdateEntity update) {
        String entityId = update.getEntityId();
        EntityStateUpdate stateUpdate = update.getStateUpdate();

        Optional<Body> foundBody = findBody(entityId);

        if (foundBody.isPresent()) {
            Body body = foundBody.get();
            updateBody(body, stateUpdate);
            world.setUpdateRequired(true);
        } else {
            System.err.println("Tried to updateEntity non-existent body: " + entityId);
        }
    }

    @Override
    public synchronized void model(CollisionResolver collisionResolver, long timeStep) {
        double timeStepInSeconds = timeStep / 1000.0;
        world.update(timeStepInSeconds);
    }

    private Body createBody(Entity entity, EntityState state) {
        Body body = new Body();
        body.setUserData(entity);

        BodyFixture fixture = new BodyFixture(fromShape(state.getShape()));
        body.addFixture(fixture);

        updateBody(body, entity, state.toStateUpdate());
        return body;
    }

    private void updateBody(Body body, Entity entity, EntityStateUpdate state) {
        BodyFixture fixture = body.getFixture(0);

        if (state.getRotation().isPresent()) {
            body.rotate(state.getRotation().get().asRad());
        }

        if (state.getShape().isPresent()) {
            //TODO update the shape
        }

        if (state.getCenter().isPresent()) {
            body.translate(fromVector(state.getCenter().get()));
            //TODO re-center the shape here?
        }

        if (state.getVelocity().isPresent()) {
            body.setLinearVelocity(fromVector(state.getVelocity().get()));
        }

        //default settings
        if (entity.isVirtual()) {
            body.setAutoSleepingEnabled(false);
            body.setMass(MassType.NORMAL);
        } else {
            body.setMass(MassType.INFINITE);
        }
        fixture.setRestitution(1.0);
        fixture.setFriction(0.0);
        body.setActive(true);
        body.setAsleep(false);
    }

    private Optional<Body> findBody(String entityId) {
        return world.getBodies().stream()
                .filter(body -> body.getId().equals(UUID.fromString(entityId)))
                .findFirst();
    }

    private void updateBody(Body body, EntityStateUpdate stateUpdate) {
        //make sure there's only one fixture
        if (body.getFixtureCount() != 1) {
            throw new IllegalStateException("We need exactly one fixture per body");
        }

        //TODO updateEntity the shape
        if (stateUpdate.getCenter().isPresent()) {
            body.translateToOrigin();
            body.translate(fromVector(stateUpdate.getCenter().get()));
            //TODO account for the size of the shape
        }
        if (stateUpdate.getVelocity().isPresent()) {
            body.setLinearVelocity(fromVector(stateUpdate.getVelocity().get()));
        }
        //TODO set the rotation
        //TODO set the restitution
    }

    private EntityInstance bodyToEntity(Body body) {
        Entity entity = (Entity) body.getUserData();

        EntityState state = new EntityStateBuilder()
            .shape(toShape(body.getFixture(0).getShape()))
            .rotation(toRadAngle(body.getTransform().getRotation()))
            .center(toVector(body.getWorldCenter()))
            .velocity(toVector(body.getLinearVelocity()))
            .build();

        return new EntityInstance(entity, state);
    }

}
