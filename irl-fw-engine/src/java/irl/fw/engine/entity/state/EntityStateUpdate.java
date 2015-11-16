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

    private Optional<ImmutableShape> shape = Optional.empty();
    private Optional<Angle> rotation = Optional.empty();
    private Optional<Vector2D> center = Optional.empty();
    private Optional<Vector2D> velocity = Optional.empty();
    private Optional<Angle> angularVelocity = Optional.empty();
    private Optional<Double> friction = Optional.empty();
    private Optional<Double> restitution = Optional.empty();

    public EntityStateUpdate() {
        //do nothing... everything should already be initialized
    }

    public EntityStateUpdate(EntityState stateToCopy) {
        shape(stateToCopy.getShape());
        rotation(stateToCopy.getRotation());
        center(stateToCopy.getCenter());
        velocity(stateToCopy.getVelocity());
        angularVelocity(stateToCopy.getAngularVelocity());
        friction(stateToCopy.getFriction());
        restitution(stateToCopy.getRestitution());
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

    public Optional<Vector2D> getVelocity() {
        return velocity;
    }

    public EntityStateUpdate angularVelocity(Angle angularVelocity) {
        this.angularVelocity = Optional.ofNullable(angularVelocity);
        return this;
    }

    public Optional<Angle> getAngularVelocity() {
        return angularVelocity;
    }

    public EntityStateUpdate friction(double friction) {
        this.friction = Optional.of(friction);
        return this;
    }

    public Optional<Double> getFriction() {
        return friction;
    }

    public EntityStateUpdate restitution(double restitution) {
        this.restitution = Optional.of(restitution);
        return this;
    }

    public Optional<Double> getRestitution() {
        return restitution;
    }

    public EntityState fillAndBuild(EntityState base) {
        return new EntityState(
            shape.orElse(base.getShape()),
            rotation.orElse(base.getRotation()),
            center.orElse(base.getCenter()),
            velocity.orElse(base.getVelocity()),
            angularVelocity.orElse(base.getAngularVelocity()),
            friction.orElse(base.getFriction()),
            restitution.orElse(base.getRestitution())
        );
    }

}
