package irl.fw.engine.entity.state;

import irl.fw.engine.geometry.ImmutableShape;
import irl.fw.engine.geometry.Vector2D;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/6/15
 */
public class EntityStateBuilder {

    protected ImmutableShape shape;
    protected Vector2D center;
    protected Vector2D velocity;

    public EntityStateBuilder() {
        //do nothing
    }

    public EntityStateBuilder shape(ImmutableShape shape) {
        this.shape = shape;
        return this;
    }

    public EntityStateBuilder center(Vector2D center) {
        this.center = center;
        return this;
    }

    public EntityStateBuilder velocity(Vector2D velocity) {
        this.velocity = velocity;
        return this;
    }

    public EntityState build() {
        return new EntityState(shape, center, velocity);
    }

    @Override
    public String toString() {
        return "Builder: " + build().toString();
    }
}
