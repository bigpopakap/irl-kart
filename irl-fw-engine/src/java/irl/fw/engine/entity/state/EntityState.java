package irl.fw.engine.entity.state;

import irl.fw.engine.geometry.Angle;
import irl.fw.engine.geometry.ImmutableShape;
import irl.fw.engine.geometry.Vector2D;

import java.awt.*;
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

    EntityState(ImmutableShape shape, Angle rotation, Vector2D center, Vector2D velocity) {
        if (shape == null || rotation == null || center == null || velocity == null) {
            throw new IllegalArgumentException("These fields must not be null");
        }

        this.shape = shape;
        this.rotation = rotation;
        this.center = center;
        this.velocity = velocity;
    }

    public ImmutableShape getShape() {
        return shape;
    }

    public ImmutableShape getTransformedShape() {
        AffineTransform transform = new AffineTransform();
        transform.rotate(getRotation().asRad());

        Vector2D center = getCenter();
        Rectangle shapeBounds = getShape().getBounds();
        transform.translate(
            center.getX() - shapeBounds.getWidth() / 2.0,
            center.getY() - shapeBounds.getHeight() / 2.0
        );
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

    @Override
    public String toString() {
        return String.format("[center:%s vel:%s]",
                getCenter(),
                getVelocity());
    }

}
