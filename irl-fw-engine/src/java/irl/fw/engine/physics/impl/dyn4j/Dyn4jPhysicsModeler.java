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
import irl.fw.engine.geometry.ImmutableShape;
import irl.fw.engine.physics.PhysicsModeler;
import irl.fw.engine.world.SimpleWorld;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.World;
import org.dyn4j.geometry.AABB;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;

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

        BodyFixture fixture = new BodyFixture(Geometry.createRectangle(20, 100));
        fixture.setRestitution(1.0);
        Body wallr = new Body();
        wallr.addFixture(fixture);
        wallr.translate(100, 80);
        wallr.setMass(MassType.INFINITE);
        world.addBody(wallr);

        world.setUpdateRequired(true);
    }

    @Override
    public irl.fw.engine.world.World getWorld() {
        Set<EntityInstance> entityInstances
            = world.getBodies().parallelStream()
                .map(this::bodyToEntity)
                .collect(Collectors.toSet());

        Set<AABB> allBounds = world.getBodies().parallelStream()
                        .map(body -> body.createAABB())
                        .collect(Collectors.toSet());

        double minX = allBounds.parallelStream()
                .map(bound -> bound.getMinX())
                .min(Double::compare)
                .orElse(0.0);

        double maxX = allBounds.parallelStream()
                .map(bound -> bound.getMaxX())
                .max(Double::compare)
                .orElse(0.0);

        double minY = allBounds.parallelStream()
                .map(bound -> bound.getMinY())
                .min(Double::compare)
                .orElse(0.0);

        double maxY = allBounds.parallelStream()
                .map(bound -> bound.getMaxY())
                .max(Double::compare)
                .orElse(0.0);

        return new SimpleWorld(entityInstances, minX, maxX, minY, maxY);
    }

    @Override
    public synchronized String add(AddEntity add) {
        Entity newEntity = add.getEntity();
        EntityState initState = add.getInitialState();
        ImmutableShape shape = initState.getShape();

        Body body = new Body();
        body.setUserData(newEntity);

        BodyFixture fixture = new BodyFixture(fromShape(shape));
        body.addFixture(fixture);

        //stuff from state
        updateBody(body, initState);

        //default settings
        body.setMass(MassType.NORMAL);
        body.setActive(true);
        body.setAsleep(false);

        //add the body
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

            updateBody(body, stateUpdate);

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

    //TODO combine the two updateBody methods somehow
    private void updateBody(Body body, EntityState state) {
        //make sure there's only one fixture
        if (body.getFixtureCount() != 1) {
            throw new IllegalStateException("We need exactly one fixture per body");
        }

        ImmutableShape shape = state.getShape();
        BodyFixture fixture = body.getFixture(0);

        body.translate(fromVector(state.getCenter()));
        body.translate(new Vector2(-shape.getBounds().getMinX(),
                -shape.getBounds().getMinY()));
        body.setLinearVelocity(fromVector(state.getVelocity()));
        //TODO set the rotation

        //TODO stuff that should be from state
        fixture.setRestitution(1.0);
    }

    private void updateBody(Body body, EntityStateUpdate stateUpdate) {
        //make sure there's only one fixture
        if (body.getFixtureCount() != 1) {
            throw new IllegalStateException("We need exactly one fixture per body");
        }

        //TODO update the shape
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
