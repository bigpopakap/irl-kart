/*
 * Copyright (c) 2010-2015 William Bittle  http://www.dyn4j.org/
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met:
 *
 *   * Redistributions of source code must retain the above copyright notice, this list of conditions
 *     and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above copyright notice, this list of conditions
 *     and the following disclaimer in the documentation and/or other materials provided with the
 *     distribution.
 *   * Neither the name of dyn4j nor the names of its contributors may be used to endorse or
 *     promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER
 * IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package irl.fw.engine.physics.impl.dyn4j;

import java.awt.geom.*;
import java.util.ArrayList;

import irl.fw.engine.geometry.ImmutableShape;
import org.dyn4j.geometry.*;
import org.dyn4j.geometry.Polygon;
import org.dyn4j.geometry.Rectangle;
import org.dyn4j.geometry.Shape;

/**
 * Graphics2D renderer for dyn4j shape types.
 * @author William Bittle, bigpopakap
 * @version 3.1.7
 * @since 3.1.5
 */
class Dyn4jShapeConverter {

    public static Convex fromShape(ImmutableShape shape) {
        if (shape == null) return null;

        Rectangle2D bounds = shape.getBounds2D();

        switch (shape.getType()) {
            case RECTANGLE:
                return new Rectangle(bounds.getWidth(), bounds.getHeight());
            case ELLIPSE:
                return new Ellipse(bounds.getWidth(), bounds.getHeight());
            case POLYGON:
                return fromPolygon(shape);
            default:
                throw new UnsupportedOperationException("This type isn't supported: " + shape.getType());
        }
    }

    private static Polygon fromPolygon(ImmutableShape shape) {
        PathIterator iter = shape.getPathIterator(null);

        ArrayList<Vector2> vertices = new ArrayList<>();

        double[] currentVertex = new double[6];
        while (!iter.isDone()) {
            if (iter.currentSegment(currentVertex) != PathIterator.SEG_CLOSE) {
                vertices.add(new Vector2(currentVertex[0], currentVertex[1]));
            }
            iter.next();
        }

        return new Polygon(vertices.toArray(new Vector2[vertices.size()]));
    }

    public static ImmutableShape toShape(Shape shape) {
        //noop
        if (shape == null) return null;

        java.awt.Shape toReturn = null;
        ImmutableShape.Type type = null;

        if (shape instanceof Rectangle) {
            type = ImmutableShape.Type.RECTANGLE;
            toReturn = toRectangle((Rectangle) shape);
        }
        else if (shape instanceof Ellipse) {
            type = ImmutableShape.Type.ELLIPSE;
            toReturn = toEllipse((Ellipse) shape);
        }
        else if (shape instanceof Polygon) {
            type = ImmutableShape.Type.POLYGON;
            toReturn = toPolygon((Polygon) shape);
        }
        else if (shape instanceof Segment) {
            type = ImmutableShape.Type.SEGMENT;
            toReturn = toSegment((Segment) shape);
//        } else if (shape instanceof Capsule) {
//            toReturn = toCapsule((Capsule) shape);
//        } else if (shape instanceof Slice) {
//            toReturn = toSlice((Slice) shape);
//        } else if (shape instanceof HalfEllipse) {
//            toReturn = toHalfEllipse((HalfEllipse) shape);
        }

        if (toReturn == null) {
            // unknown shape
            throw new UnsupportedOperationException("Unknown shape cannot be converted: " + shape.getClass());
        } else {
            return new ImmutableShape(type, toReturn);
        }
    }

    public static java.awt.Shape toRectangle(Rectangle rectangle) {
        return new Rectangle2D.Double(
            0.0, 0.0,
            rectangle.getWidth(),
            rectangle.getHeight()
        );
    }

    public static java.awt.Shape toEllipse(Ellipse ellipse) {
        return new Ellipse2D.Double(
            0, 0, ellipse.getWidth(), ellipse.getHeight()
        );
    }

    public static java.awt.Shape toPolygon(Polygon polygon) {
        Vector2[] vertices = polygon.getVertices();
        int l = vertices.length;

        // create the awt polygon
        Path2D.Double p = new Path2D.Double();
        p.moveTo(vertices[0].x, vertices[0].y);
        for (int i = 1; i < l; i++) {
            p.lineTo(vertices[i].x, vertices[i].y);
        }
        p.closePath();

        return p;
    }

    public static java.awt.Shape toSegment(Segment segment) {
        Vector2[] vertices = segment.getVertices();

        Line2D.Double l = new Line2D.Double(
                vertices[0].x,
                vertices[0].y,
                vertices[1].x,
                vertices[1].y);

        return l;
    }

//    public static java.awt.Shape toCapsule(Capsule capsule) {
//        // get the local rotation and translation
//        double rotation = capsule.getRotation();
//        Vector2 center = capsule.getCenter();
//
//        // save the old transform
//        AffineTransform oTransform = g.getTransform();
//        // translate and rotate
//        g.translate(center.x * scale, center.y * scale);
//        g.rotate(rotation);
//
//        double width = capsule.getLength();
//        double radius = capsule.getCapRadius();
//        double radius2 = radius * 2.0;
//
//        Arc2D.Double arcL = new Arc2D.Double(
//                -(width * 0.5) * scale,
//                -radius * scale,
//                radius2 * scale,
//                radius2 * scale,
//                90.0,
//                180.0,
//                Arc2D.OPEN);
//        Arc2D.Double arcR = new Arc2D.Double(
//                (width * 0.5 - radius2) * scale,
//                -radius * scale,
//                radius2 * scale,
//                radius2 * scale,
//                -90.0,
//                180.0,
//                Arc2D.OPEN);
//
//        // connect the shapes
//        Path2D.Double path = new Path2D.Double();
//        path.append(arcL, true);
//        path.append(new Line2D.Double(arcL.getEndPoint(), arcR.getStartPoint()), true);
//        path.append(arcR, true);
//        path.append(new Line2D.Double(arcR.getEndPoint(), arcL.getStartPoint()), true);
//
//        // set the color
//        g.setColor(color);
//        // fill the shape
//        g.fill(path);
//        // set the color
//        g.setColor(getOutlineColor(color));
//        // draw the shape
//        g.draw(path);
//
//        // re-instate the old transform
//        g.setTransform(oTransform);
//    }

//    /**
//     * Renders the given {@link Slice} to the given graphics context using the given scale and color.
//     * @param g the graphics context
//     * @param slice the slice to render
//     * @param scale the scale to render the shape (pixels per dyn4j unit (typically meter))
//     * @param color the color
//     */
//    public static final void render(Graphics2D g, Slice slice, double scale, Color color) {
//        double radius = slice.getSliceRadius();
//        double theta2 = slice.getTheta() * 0.5;
//
//        // get the local rotation and translation
//        double rotation = slice.getRotation();
//        Vector2 circleCenter = slice.getCircleCenter();
//
//        // save the old transform
//        AffineTransform oTransform = g.getTransform();
//        // translate and rotate
//        g.translate(circleCenter.x * scale, circleCenter.y * scale);
//        g.rotate(rotation);
//
//        // to draw the arc, java2d wants the top left x,y
//        // as if you were drawing a circle
//        Arc2D a = new Arc2D.Double(-radius * scale,
//                -radius * scale,
//                2.0 * radius * scale,
//                2.0 * radius * scale,
//                -Math.toDegrees(theta2),
//                Math.toDegrees(2.0 * theta2),
//                Arc2D.PIE);
//
//        // fill the shape
//        g.setColor(color);
//        g.fill(a);
//        // draw the outline
//        g.setColor(getOutlineColor(color));
//        g.draw(a);
//
//        // re-instate the old transform
//        g.setTransform(oTransform);
//    }

//    /**
//     * Renders the given {@link HalfEllipse} to the given graphics context using the given scale and color.
//     * @param g the graphics context
//     * @param halfEllipse the halfEllipse to render
//     * @param scale the scale to render the shape (pixels per dyn4j unit (typically meter))
//     * @param color the color
//     */
//    public static final void render(Graphics2D g, HalfEllipse halfEllipse, double scale, Color color) {
//        double width = halfEllipse.getWidth();
//        double height = halfEllipse.getHeight();
//
//        // get the local rotation and translation
//        double rotation = halfEllipse.getRotation();
//        Vector2 center = halfEllipse.getEllipseCenter();
//
//        // save the old transform
//        AffineTransform oTransform = g.getTransform();
//        // translate and rotate
//        g.translate(center.x * scale, center.y * scale);
//        g.rotate(rotation);
//
//        // to draw the arc, java2d wants the top left x,y
//        // as if you were drawing a circle
//        Arc2D a = new Arc2D.Double(
//                (-width * 0.5) * scale,
//                -height * scale,
//                width * scale,
//                height * 2.0 * scale,
//                0,
//                -180.0,
//                Arc2D.PIE);
//
//        // fill the shape
//        g.setColor(color);
//        g.fill(a);
//        // draw the outline
//        g.setColor(getOutlineColor(color));
//        g.draw(a);
//
//        // re-instate the old transform
//        g.setTransform(oTransform);
//    }

}
