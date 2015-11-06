package irl.fw.engine.entity.state;

import irl.fw.engine.geometry.Angle;
import irl.fw.engine.geometry.ImmutableShape;
import irl.fw.engine.geometry.Vector2D;

import java.util.Optional;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/6/15
 */
public class EntityStateUpdate {

    private Optional<ImmutableShape> shape;
    private Optional<Angle> rotation;
    private Optional<Vector2D> center;
    private Optional<Vector2D> velocity;

    public EntityStateUpdate() {
        shape = Optional.empty();
        rotation = Optional.empty();
        center = Optional.empty();
        velocity = Optional.empty();
    }

    public EntityStateUpdate shape(ImmutableShape shape) {
        this.shape = Optional.ofNullable(shape);
        return this;
    }

    public EntityStateUpdate rotation(Angle rotation) {
        this.rotation = Optional.ofNullable(rotation);
        return this;
    }

    public EntityStateUpdate center(Vector2D center) {
        this.center = Optional.ofNullable(center);
        return this;
    }

    public EntityStateUpdate velocity(Vector2D velocity) {
        this.velocity = Optional.ofNullable(velocity);
        return this;
    }

    public EntityState fillAndBuild(EntityState base) {
        return new EntityState(
            shape.isPresent() ? shape.get() : base.getShape(),
            rotation.isPresent() ? rotation.get() : base.getRotation(),
            center.isPresent() ? center.get() : base.getCenter(),
            velocity.isPresent() ? velocity.get() : base.getVelocity()
        );
    }

}