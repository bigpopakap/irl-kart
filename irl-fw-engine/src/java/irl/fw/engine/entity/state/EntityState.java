package irl.fw.engine.entity.state;

import irl.fw.engine.geometry.ImmutableShape;
import irl.fw.engine.geometry.Vector2D;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 10/29/15
 */
public class EntityState {

    private final ImmutableShape shape;
    private final Vector2D center;
    private final Vector2D velocity;

    EntityState(ImmutableShape shape, Vector2D center, Vector2D velocity) {
        this.shape = shape;
        this.center = center;
        this.velocity = velocity;
    }

    public ImmutableShape getShape() {
        return shape;
    }

    public Vector2D getCenter() {
        return center;
    }

    public Vector2D getVelocity() {
        return velocity;
    }

    @Override
    public String toString() {
        return String.format("[center:%s vel:%s]",
                getCenter(),
                getVelocity());
    }

}
