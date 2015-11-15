package irl.fw.engine.graphics.impl.swing;

import irl.fw.engine.geometry.ImmutableShape;
import irl.fw.engine.world.World;

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
        g2.setColor(Color.BLACK);
        drawArrow(g, 0, 0, 400, 0);
        drawArrow(g, 0, 0, 0, 200);

        //TODO transform the graphics so the world is scaled
//            transform.translate(world.getMinX(), world.getMinY());
//            transform.scale(getWidth() / world.getWidth(),
//                            getHeight() / world.getHeight());
        g2.setTransform(transform);

        //draw bounds around the world
        g2.setColor(Color.GREEN);
        g2.draw(new Rectangle2D.Double(world.getMinX(), world.getMinY(),
                world.getWidth(), world.getHeight()));
        g2.setColor(Color.BLACK);

        //draw all the items in the world
        world.getEntities().stream()
                .map(entity -> entity.getState().getTransformedShape())
                .forEach(shape -> draw(g2, shape));

        //revert back to the original transform
        g2.setTransform(savedTrans);
    }

    private void draw(Graphics2D g2, ImmutableShape shape) {
        g2.draw(shape);
    }

    private void drawArrow(Graphics g1, int x1, int y1, int x2, int y2) {
        final int ARR_SIZE = 4;

        Graphics2D g = (Graphics2D) g1.create();

        double dx = x2 - x1, dy = y2 - y1;
        double angle = Math.atan2(dy, dx);
        int len = (int) Math.sqrt(dx * dx + dy * dy);
        AffineTransform at = AffineTransform.getTranslateInstance(x1, y1);
        at.concatenate(AffineTransform.getRotateInstance(angle));
        g.transform(at);

        // Draw horizontal arrow starting in (0, 0)
        g.drawLine(0, 0, len, 0);
        g.fillPolygon(new int[]{len, len - ARR_SIZE, len - ARR_SIZE, len},
                new int[]{0, -ARR_SIZE, ARR_SIZE, 0}, 4);
    }
}
