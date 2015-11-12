package irl.fw.engine.entity.state;

import irl.fw.engine.geometry.Angle;
import irl.fw.engine.geometry.ImmutableShape;
import irl.fw.engine.geometry.Vector2D;

import java.awt.geom.AffineTransform;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 10/29/15
 */
public class EntityState {

    private final ImmutableShape shape;
    private final Angle rotation;
    private final Vector2D center;
    private final Vector2D velocity;
    private final Angle angularVelocity;

    EntityState(ImmutableShape shape, Angle rotation,
                Vector2D center, Vector2D velocity,
                Angle angularVelocity) {
        if (shape == null || rotation == null || center == null
            || velocity == null || angularVelocity == null) {
            throw new IllegalArgumentException("These fields must not be null");
        }

        this.shape = shape.centerAtOrigin();
        this.rotation = rotation;
        this.center = center;
        this.velocity = velocity;
        this.angularVelocity = angularVelocity;
    }

    public ImmutableShape getShape() {
        return shape;
    }

    public ImmutableShape getTransformedShape() {
        AffineTransform transform = new AffineTransform();

        Vector2D center = getCenter();
        transform.translate(center.getX(), center.getY());
        transform.rotate(getRotation().asRad());

        return shape.transform(transform);
    }

    public Angle getRotation() {
        return rotation;
    }

    public Vector2D getCenter() {
        return center;
    }

    public Vector2D getVelocity() {
        return velocity;
    }

    public Angle getAngularVelocity() {
        return angularVelocity;
    }

    public EntityStateUpdate toStateUpdate() {
        return new EntityStateUpdate(this);
    }

    @Override
    public String toString() {
        return String.format("[center:%s vel:%s]",
                getCenter(),
                getVelocity());
    }

}
