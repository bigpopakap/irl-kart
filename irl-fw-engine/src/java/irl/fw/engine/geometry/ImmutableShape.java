package irl.fw.engine.geometry;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/6/15
 */
public class ImmutableShape implements Shape {

    public enum Type {
        RECTANGLE, ELLIPSE, SEGMENT, CONVEX_POLY
    }

    private final Type type;
    private final Shape shape;

    /**
     * Make sure you don't change the input once you've
     * passed it in here
     */
    public ImmutableShape(Type type, Shape shape) {
        if (type == null) {
            throw new IllegalArgumentException("The shape must have a type");
        }

        this.type = type;
        this.shape = shape;
    }

    public Type getType() {
        return type;
    }

    public ImmutableShape transform(AffineTransform transform) {
        return new ImmutableShape(getType(), transform.createTransformedShape(this));
    }

    /**
     * Translates the shape so its center is at the origin
     * @return
     */
    public ImmutableShape centerAtOrigin() {
        Rectangle shapeBounds = getBounds();
        AffineTransform trans = new AffineTransform();
        trans.translate(
            -shapeBounds.getCenterX(),
            -shapeBounds.getCenterY()
        );
        return transform(trans);
    }

    @Override
    public Rectangle getBounds() {
        return shape.getBounds();
    }

    @Override
    public Rectangle2D getBounds2D() {
        return shape.getBounds2D();
    }

    @Override
    public boolean contains(double x, double y) {
        return shape.contains(x, y);
    }

    @Override
    public boolean contains(Point2D p) {
        return shape.contains(p);
    }

    @Override
    public boolean intersects(double x, double y, double w, double h) {
        return shape.intersects(x, y, w, h);
    }

    @Override
    public boolean intersects(Rectangle2D r) {
        return shape.intersects(r);
    }

    @Override
    public boolean contains(double x, double y, double w, double h) {
        return shape.contains(x, y, w, h);
    }

    @Override
    public boolean contains(Rectangle2D r) {
        return shape.contains(r);
    }

    @Override
    public PathIterator getPathIterator(AffineTransform at) {
        return shape.getPathIterator(at);
    }

    @Override
    public PathIterator getPathIterator(AffineTransform at, double flatness) {
        return shape.getPathIterator(at, flatness);
    }
}
