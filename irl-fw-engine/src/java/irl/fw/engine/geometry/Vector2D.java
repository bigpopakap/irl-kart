package irl.fw.engine.geometry;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/6/15
 */
public class Vector2D {

    public static final Vector2D ZERO = new Vector2D(0, 0);

    private final Point2D point;

    public Vector2D(Point2D point) {
        this.point = (Point2D) point.clone();
    }

    public Vector2D(int x, int y) {
        this(new Point(x, y));
    }

    public Vector2D(double x, double y) {
        this(new Point2D.Double(x, y));
    }

    public double getX() {
        return point.getX();
    }

    public double getY() {
        return point.getY();
    }

    public double getMagnitude() {
        return Math.sqrt(getX()*getX() + getY()*getY());
    }

    public Vector2D add(Vector2D other) {
        return new Vector2D(getX() + other.getX(),
                            getY() + other.getY());
    }

    public Vector2D subtract(Vector2D other) {
        return new Vector2D(getX() - other.getX(),
                            getY() - other.getY());
    }

    public Vector2D multiply(double scalar) {
        return new Vector2D(scalar * getX(), scalar * getY());
    }

    public Vector2D scaleTo(double magnitude) {
        return multiply(magnitude / getMagnitude());
    }

    public Vector2D rotate(Angle angle) {
        double rads = angle.asRad();

        double cos = Math.cos(rads);
        double sin = Math.sin(rads);

        return new Vector2D(
            getX()*cos - getY()*sin,
            getY()*cos + getX()*sin
        );
    }

    @Override
    public String toString() {
        return String.format("(%s, %s)", getX(), getY());
    }
}
