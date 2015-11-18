package irl.fw.engine.physics.impl.dyn4j;

import irl.fw.engine.collisions.CollisionResolver;
import irl.fw.engine.entity.Entity;
import irl.fw.engine.events.EntityCollision;
import org.dyn4j.collision.manifold.Manifold;
import org.dyn4j.collision.narrowphase.Penetration;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.CollisionListener;
import org.dyn4j.dynamics.contact.*;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/11/15
 */
class CollisionResolverAdaptor implements CollisionListener, ContactListener {

    private final Dyn4jEntityConverter entityConverter;
    private final CollisionResolver resolver;

    public CollisionResolverAdaptor(Dyn4jEntityConverter entityConverter, CollisionResolver resolver) {
        this.entityConverter = entityConverter;
        this.resolver = resolver;
    }

    @Override
    public boolean collision(Body body1, BodyFixture fixture1, Body body2, BodyFixture fixture2) {
        return true;
    }

    @Override
    public boolean collision(Body body1, BodyFixture fixture1, Body body2, BodyFixture fixture2, Penetration penetration) {
        return true;
    }

    @Override
    public boolean collision(Body body1, BodyFixture fixture1, Body body2, BodyFixture fixture2, Manifold manifold) {
        return true;
    }

    @Override
    public boolean collision(ContactConstraint contactConstraint) {
        //do nothing
        return true;
    }

    @Override
    public void sensed(ContactPoint point) {
        //do nothing
    }

    @Override
    public boolean begin(ContactPoint point) {
        Entity entity1 = entityConverter.toEntity(point.getBody1());
        Entity entity2 = entityConverter.toEntity(point.getBody2());

        EntityCollision event = new EntityCollision(entity1, entity2);

        boolean entity1Result = entity1.collide(entity2);
        boolean entity2Result = entity2.collide(entity1);
        boolean resolverResult = resolver.onCollision(event);

        return entity1Result && entity2Result && resolverResult;
    }

    @Override
    public void end(ContactPoint point) {
        Entity entity1 = entityConverter.toEntity(point.getBody1());
        Entity entity2 = entityConverter.toEntity(point.getBody2());

//        TODO uncomment if using collision resolver
//        EntityCollision event = new EntityCollision(entity1, entity2);

        entity1.afterCollide(entity2);
        entity2.afterCollide(entity1);
        //TODO call a collision resolver?
    }

    @Override
    public boolean persist(PersistedContactPoint point) {
        //do nothing
        return true;
    }

    @Override
    public boolean preSolve(ContactPoint point) {
        //do nothing
        return true;
    }

    @Override
    public void postSolve(SolvedContactPoint point) {
        //do nothing
    }
}
