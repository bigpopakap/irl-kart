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
    private Optional<Angle> angularVelocity;

    public EntityStateUpdate() {
        shape = Optional.empty();
        rotation = Optional.empty();
        center = Optional.empty();
        velocity = Optional.empty();
        angularVelocity = Optional.empty();
    }

    public EntityStateUpdate(EntityState stateToCopy) {
        shape(stateToCopy.getShape());
        rotation(stateToCopy.getRotation());
        center(stateToCopy.getCenter());
        velocity(stateToCopy.getVelocity());
        angularVelocity(stateToCopy.getAngularVelocity());
    }

    public EntityStateUpdate shape(ImmutableShape shape) {
        this.shape = Optional.ofNullable(shape);
        return this;
    }

    public Optional<ImmutableShape> getShape() {
        return shape;
    }

    public EntityStateUpdate rotation(Angle rotation) {
        this.rotation = Optional.ofNullable(rotation);
        return this;
    }

    public Optional<Angle> getRotation() {
        return rotation;
    }

    public EntityStateUpdate center(Vector2D center) {
        this.center = Optional.ofNullable(center);
        return this;
    }

    public Optional<Vector2D> getCenter() {
        return center;
    }

    public EntityStateUpdate velocity(Vector2D velocity) {
        this.velocity = Optional.ofNullable(velocity);
        return this;
    }

    public EntityStateUpdate angularVelocity(Angle angularVelocity) {
        this.angularVelocity = Optional.ofNullable(angularVelocity);
        return this;
    }

    public Optional<Vector2D> getVelocity() {
        return velocity;
    }

    public EntityState fillAndBuild(EntityState base) {
        return new EntityState(
            shape.orElse(base.getShape()),
            rotation.orElse(base.getRotation()),
            center.orElse(base.getCenter()),
            velocity.orElse(base.getVelocity()),
            angularVelocity.orElse(base.getAngularVelocity())
        );
    }

}
