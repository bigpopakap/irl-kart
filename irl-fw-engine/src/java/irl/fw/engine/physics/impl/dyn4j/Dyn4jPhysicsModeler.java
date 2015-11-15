package irl.fw.engine.physics.impl.dyn4j;

import irl.fw.engine.entity.Entity;
import irl.fw.engine.entity.factory.EntityFactory;
import irl.fw.engine.entity.EntityId;
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
import org.dyn4j.dynamics.*;
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
        Set<Entity> entityInstances
            = world.getBodies().parallelStream()
                .map(body -> (Entity) body.getUserData())
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
    public synchronized void addEntity(AddEntity add) {
        Body body = createBody(add.getEntityFactory());
        world.addBody(body);
        world.setUpdateRequired(true);
    }

    @Override
    public synchronized void removeEntity(RemoveEntity remove) {
        EntityId entityId = remove.getEntityId();
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
        EntityId entityId = update.getEntityId();
        EntityStateUpdate stateUpdate = update.getStateUpdate();

        Optional<Body> foundBody = findBody(entityId);

        if (foundBody.isPresent()) {
            Body body = foundBody.get();
            Entity entity = (Entity) body.getUserData();
            updateBody(body, entity, stateUpdate);
            world.setUpdateRequired(true);
        } else {
            System.err.println("Tried to updateEntity non-existent body: " + entityId);
        }
    }

    @Override
    public synchronized void model(CollisionResolver collisionResolver, long timeStep) {
        double timeStepInSeconds = timeStep / 1000.0;

        world.removeAllListeners(); //TODO don't be so heavy-handed
        world.addListener(new CollisionResolverAdaptor(collisionResolver));

        world.update(timeStepInSeconds);

        //update all the associated entity states
        world.getBodies().parallelStream()
                .forEach(body -> updateEntityData(body));
    }

    private Body createBody(EntityFactory<? extends Entity> entityFactory) {
        Body body = new Body();

        Entity entity = entityFactory.create(body.getId().toString());
        EntityState state = entity.getState();
        body.setUserData(entity);

        BodyFixture fixture = new BodyFixture(fromShape(state.getShape()));
        body.addFixture(fixture);

        updateBody(body, entity, state.toStateUpdate());

        return body;
    }

    private void updateBody(Body body, Entity entity, EntityStateUpdate state) {
        BodyFixture fixture = body.getFixture(0);

        if (state.getRotation().isPresent()) {
            double rotDiff = state.getRotation().get().asRad() -
                            body.getTransform().getRotation();
            body.rotateAboutCenter(rotDiff);
        }

        if (state.getShape().isPresent()) {
            //TODO change the shape
        }

        if (state.getCenter().isPresent()) {
            body.translate(fromVector(state.getCenter().get()));
            //TODO re-center the shape here?
        }

        if (state.getVelocity().isPresent()) {
            body.setLinearVelocity(fromVector(state.getVelocity().get()));
        }

        if (state.getAngularVelocity().isPresent()) {
            body.setAngularVelocity(state.getAngularVelocity().get().asRad());
        }

        //default settings
        if (entity.isVirtual()) {
            body.setAutoSleepingEnabled(false);
            //TODO should shells have fixed angular velocity?
            body.setMass(MassType.NORMAL);
        } else {
            body.setMass(MassType.INFINITE);
        }
        fixture.setRestitution(1.0);
        fixture.setFriction(0.0);
        body.setActive(true);
        body.setAsleep(false);
    }

    private void updateEntityData(Body body) {
        Entity entity = (Entity) body.getUserData();

        entity.setState(new EntityStateBuilder()
                .shape(toShape(body.getFixture(0).getShape()))
                .rotation(toRadAngle(body.getTransform().getRotation()))
                .center(toVector(body.getWorldCenter()))
                .velocity(toVector(body.getLinearVelocity()))
                .angularVelocity(toRadAngle(body.getAngularVelocity()))
                .build());
    }

    private Optional<Body> findBody(EntityId entityId) {
        return world.getBodies().stream()
                .filter(body -> body.getId().equals(fromId(entityId)))
                .findFirst();
    }

}
