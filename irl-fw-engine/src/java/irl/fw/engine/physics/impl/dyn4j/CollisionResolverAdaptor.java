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

    private final CollisionResolver resolver;

    public CollisionResolverAdaptor(CollisionResolver resolver) {
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
        Entity entity1 = (Entity) body1.getUserData();
        Entity entity2 = (Entity) body2.getUserData();

        EntityCollision event = new EntityCollision(entity1, entity2);

        return resolver.onBeforeCollision(event);
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
        //do nothing
        return true;
    }

    @Override
    public void end(ContactPoint point) {
        Entity entity1 = (Entity) point.getBody1().getUserData();
        Entity entity2 = (Entity) point.getBody2().getUserData();

        EntityCollision event = new EntityCollision(entity1, entity2);

        resolver.onCollision(event);
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
