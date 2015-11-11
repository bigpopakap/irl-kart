package irl.fw.engine.physics.impl.dyn4j;

import irl.fw.engine.collisions.CollisionResolver;
import irl.fw.engine.entity.EntityInstance;
import irl.fw.engine.events.EntityCollision;
import org.dyn4j.collision.manifold.Manifold;
import org.dyn4j.collision.narrowphase.Penetration;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.CollisionListener;
import org.dyn4j.dynamics.contact.ContactConstraint;

import static irl.fw.engine.physics.impl.dyn4j.Dyn4jEntityConverter.*;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/11/15
 */
class CollisionResolverAdaptor implements CollisionListener {

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
        EntityInstance entity1 = toEntity(body1);
        EntityInstance entity2 = toEntity(body2);

        EntityCollision event = new EntityCollision(entity1, entity2);

        return resolver.onBeforeCollision(event);
    }

    @Override
    public boolean collision(ContactConstraint contactConstraint) {
        EntityInstance entity1 = toEntity(contactConstraint.getBody1());
        EntityInstance entity2 = toEntity(contactConstraint.getBody2());

        EntityCollision event = new EntityCollision(entity1, entity2);

        resolver.onCollision(event);

        return true;
    }

}
