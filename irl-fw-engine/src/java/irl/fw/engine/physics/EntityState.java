package irl.fw.engine.physics;

import org.dyn4j.geometry.Vector2;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 10/29/15
 */
public class EntityState {

    private final Vector2 center;
    private final Vector2 velocity;

    public EntityState(Vector2 center, Vector2 velocity) {
        this.center = center;
        this.velocity = velocity;
    }

    public Vector2 getCenter() {
        return center;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    @Override
    public String toString() {
        return String.format("[center:%s vel:%s]",
                getCenter(),
                getVelocity());
    }
}
