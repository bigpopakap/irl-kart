package irl.fw.engine.graphics.impl.swing;

import irl.fw.engine.entity.Entity;
import irl.fw.engine.entity.factory.EntityDisplayConfig;
import irl.fw.engine.entity.state.EntityState;
import irl.fw.engine.geometry.Vector2D;
import irl.fw.engine.world.World;
import irl.util.ColorUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/14/15
 */
class MyPanel extends JPanel {

    public static final long serialVersionUID = 1L;

    private static final boolean DRAW_VELOCITY = true;

    private volatile World world;

    public synchronized void update(World world) {
        this.world = world;
        repaint();
    }

    @Override
    public synchronized void paint(Graphics g) {
        super.paint(g);

        if (world == null) return;

        Graphics2D g2 = (Graphics2D) g;
        AffineTransform savedTrans = g2.getTransform();

        //translate so the axes are normal-people axes
        AffineTransform transform = new AffineTransform();
        transform.translate(0, getHeight());
        transform.scale(1, -1);
        {
            final int PADDING = 10;
            transform.translate(PADDING, PADDING);
            transform.scale((getWidth() - 2.0 * PADDING) / getWidth(),
                    (getHeight() - 2.0 * PADDING) / getHeight());
        }
        g2.setTransform(transform);

        //draw axes
        g2.setColor(Color.RED);
        g2.drawRect(0, 0, getWidth(), getHeight());
        drawArrow(g2, 0, 0, 400, 0);
        drawArrow(g2, 0, 0, 0, 200);

        {
            double scale = Math.min(getWidth() / world.getWidth(),
                                    getHeight() / world.getHeight());
            transform.scale(scale, scale);
        }
        transform.translate(-world.getMinX(), -world.getMinY());
        g2.setTransform(transform);

        //draw bounds around the world
        g2.setColor(Color.GREEN);
        g2.draw(new Rectangle2D.Double(world.getMinX(), world.getMinY(),
                world.getWidth(), world.getHeight()));

        //draw all the items in the world
        world.getEntities().forEach(entity -> draw(g2, entity));

        //revert back to the original transform
        g2.setTransform(savedTrans);
    }

    private void draw(Graphics2D g2, Entity entity) {
        EntityState state = entity.getState();
        Vector2D center = state.getCenter();
        Shape shape = state.getTransformedShape();
        EntityDisplayConfig display = entity.getDisplayConfig();

        //fill in the shape
        g2.setColor(display.getFillColor());
        g2.fill(shape);

        //draw the outline
        //TODO rotate the text so it's upright
        g2.setColor(display.getOutlineColor());
        g2.setStroke(new BasicStroke(1));
        g2.draw(shape);

        //draw the label text
        g2.setColor(display.getLabelColor());
        g2.drawString(display.getLabel(),
                (int) center.getX(), (int) center.getY());

        //draw the velocity vector
        Vector2D velocity = state.getVelocity();
        if (DRAW_VELOCITY && velocity.getMagnitude() > 0) {
            g2.setColor(ColorUtils.makeTransparent(
                display.getVelocityColor(),
                0.3
            ));
            g2.setStroke(new BasicStroke(1));
            Vector2D arrowEnd = center.add(velocity);
            drawArrow(g2, (int) center.getX(), (int) center.getY(),
                    (int) arrowEnd.getX(), (int) arrowEnd.getY());
        }
    }

    private void drawArrow(Graphics2D g2, int x1, int y1, int x2, int y2) {
        final int ARR_SIZE = 4;

        AffineTransform origTans = g2.getTransform();

        double dx = x2 - x1, dy = y2 - y1;
        double angle = Math.atan2(dy, dx);
        int len = (int) Math.sqrt(dx * dx + dy * dy);
        AffineTransform at = AffineTransform.getTranslateInstance(x1, y1);
        at.concatenate(AffineTransform.getRotateInstance(angle));
        g2.transform(at);

        // Draw horizontal arrow starting in (0, 0)
        g2.drawLine(0, 0, len, 0);
        g2.fillPolygon(new int[]{len, len - ARR_SIZE, len - ARR_SIZE, len},
                new int[]{0, -ARR_SIZE, ARR_SIZE, 0}, 4);

        g2.setTransform(origTans);
    }
}
