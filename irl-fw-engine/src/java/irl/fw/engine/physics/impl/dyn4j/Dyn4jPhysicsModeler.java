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

        //FIXME these should be added by SwingWorld
        addWalls(new Rectangle(400, 300));
        BodyFixture shellFixture = new BodyFixture(Geometry.createCircle(5));
        shellFixture.setRestitution(1);
        shellFixture.setFriction(0);
        Body shell = new Body();
        shell.addFixture(shellFixture);
        shell.setMass(MassType.NORMAL);
        shell.setLinearVelocity(50, 40);
        shell.translate(75, 75);
        world.addBody(shell);
        world.setUpdateRequired(true);
    }

    private void addWalls(Rectangle worldBounds) {
        final double WALL_THICKNESS = 20;

        //left wall
        BodyFixture fixtureL = new BodyFixture(Geometry.createRectangle(WALL_THICKNESS, worldBounds.getHeight()));
        fixtureL.setRestitution(1.0);
        Body wallL = new Body();
        wallL.addFixture(fixtureL);
        wallL.setMass(MassType.INFINITE);
        wallL.translate(WALL_THICKNESS / 2, worldBounds.getHeight() / 2);
        world.addBody(wallL);
        world.setUpdateRequired(true);

        //right wall
        BodyFixture fixtureR = new BodyFixture(Geometry.createRectangle(WALL_THICKNESS, worldBounds.getHeight()));
        fixtureL.setRestitution(1.0);
        Body wallR = new Body();
        wallR.addFixture(fixtureR);
        wallR.setMass(MassType.INFINITE);
        wallR.translate(WALL_THICKNESS / 2, worldBounds.getHeight() / 2);
        wallR.translate(worldBounds.getWidth() - WALL_THICKNESS, 0);
        world.addBody(wallR);
        world.setUpdateRequired(true);

        //top wall
        BodyFixture fixtureT = new BodyFixture(Geometry.createRectangle(worldBounds.getWidth() - 2*WALL_THICKNESS, WALL_THICKNESS));
        fixtureL.setRestitution(1.0);
        Body wallT = new Body();
        wallT.addFixture(fixtureT);
        wallT.setMass(MassType.INFINITE);
        wallT.translate(worldBounds.getWidth()/2 - WALL_THICKNESS, WALL_THICKNESS / 2);
        wallT.translate(WALL_THICKNESS, worldBounds.getHeight() - WALL_THICKNESS);
        world.addBody(wallT);
        world.setUpdateRequired(true);

        //bottom wall
        BodyFixture fixtureB = new BodyFixture(Geometry.createRectangle(worldBounds.getWidth() - 2*WALL_THICKNESS, WALL_THICKNESS));
        fixtureL.setRestitution(1.0);
        Body wallB = new Body();
        wallB.addFixture(fixtureB);
        wallB.setMass(MassType.INFINITE);
        wallB.translate(worldBounds.getWidth() / 2 - WALL_THICKNESS, WALL_THICKNESS / 2);
        wallB.translate(WALL_THICKNESS, 0);
        world.addBody(wallB);
        world.setUpdateRequired(true);
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
        body.setAutoSleepingEnabled(false); //TODO should this be for all objects?
        body.setMass(MassType.INFINITE); //TODO this shouldn't be for all objects
        body.setActive(true);
        body.setAsleep(false);

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
        fixture.setFriction(0.0);
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
