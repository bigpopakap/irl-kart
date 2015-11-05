package irl.fw.engine.physics;

import org.dyn4j.geometry.Convex;
import org.dyn4j.geometry.Vector2;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 10/29/15
 */
public class EntityState {

    private final Convex shape;
    private final Vector2 velocity;

    public EntityState(Convex shape, Vector2 velocity) {
        this.shape = shape;
        this.velocity = velocity;
    }

    public Convex getShape() {
        return shape;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    @Override
    public String toString() {
        return String.format("[shape:%s vel:%s]",
                getShape().getClass().getSimpleName(),
                getVelocity());
    }
}
