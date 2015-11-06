package irl.kart.world;

import irl.fw.engine.entity.state.EntityStateUpdate;
import irl.fw.engine.geometry.Vector2D;
import irl.fw.engine.world.World;
import irl.kart.beacon.KartBeacon;
import irl.kart.beacon.KartUpdate;
import irl.fw.engine.graphics.Renderer;
import irl.util.callbacks.Callback;
import irl.util.callbacks.Callbacks;
import irl.util.concurrent.StoppableRunnable;
import irl.util.reactiveio.Pipe;
import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/2/15
 */
public class SwingWorld implements KartBeacon, Renderer, StoppableRunnable {

    private final String kart1Id;
    private final String kart2Id;

    private final Callbacks onStop;

    private final Subject<KeyEvent, KeyEvent> rawPositions;
    private final Pipe<KartUpdate> updates;
    private volatile int kart1Position = 0;
    private volatile int kart2Position = 0;

    private volatile boolean isStopped = true;
    private JFrame frame;
    private MyPanel panel;

    public SwingWorld(String kart1Id, String kart2Id) {
        this.kart1Id = kart1Id;
        this.kart2Id = kart2Id;
        this.rawPositions = PublishSubject.<KeyEvent>create().toSerialized();

        this.updates = new Pipe<>();
        this.updates.mergeIn(rawPositions
                .map(this::keyEventToUpdate)
                .filter(update -> update != null));

        onStop = new Callbacks();
    }

    @Override
    public synchronized void stop() {
        if (!isStopped()) {
            frame.dispose();
            isStopped = true;
            onStop.run();
        }
    }

    @Override
    public boolean isStopped() {
        return isStopped;
    }

    @Override
    public String onStop(Callback callback) {
        return onStop.add(callback);
    }

    @Override
    public void run() {
        isStopped = false;

        this.frame = new JFrame();
        this.panel = new MyPanel();
        frame.add(panel);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                stop();
            }
        });

        panel.setFocusable(true);
        panel.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                //do nothing
            }

            @Override
            public void keyPressed(KeyEvent e) {
                rawPositions.onNext(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                //do nothing
            }
        });

        frame.setSize(800, 400);
        panel.setSize(800, 400);
        frame.setVisible(true);
        panel.setVisible(true);
        panel.requestFocusInWindow();
    }

    @Override
    public Observable<KartUpdate> updates() {
        return updates.get();
    }

    @Override
    public void render(World world, long timeSinceLastUpdate) {
        if (frame != null && panel != null) {
            panel.update(world);
            frame.repaint();
        }
    }

    private KartUpdate keyEventToUpdate(KeyEvent evt) {
        switch (evt.getKeyCode()) {
            case KeyEvent.VK_UP:
                return makeKartUpdate(kart1Id, ++kart1Position);
            case KeyEvent.VK_DOWN:
                return makeKartUpdate(kart1Id, --kart1Position);
            case KeyEvent.VK_W:
                return makeKartUpdate(kart2Id, ++kart2Position);
            case KeyEvent.VK_S:
                return makeKartUpdate(kart2Id, --kart2Position);
            default:
                return null;
        }
    }

    private KartUpdate makeKartUpdate(String kartId, int value) {
        return new KartUpdate(
            kartId,
            new EntityStateUpdate()
                .center(new Vector2D(value, value))
                .velocity(new Vector2D(value, value))
        );
    }

    private static class MyPanel extends JPanel {

        public static final long serialVersionUID = 1L;

        private World world;

        public synchronized void update(World world) {
            this.world = world;
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);

            final int PADDING = 10;

            Graphics2D g2 = (Graphics2D) g;
            AffineTransform savedTrans = g2.getTransform();

            //translate so the axes are normal-people axes
            AffineTransform transform = new AffineTransform();
            transform.translate(0, getHeight());
            transform.scale(1, -1);
            transform.translate(PADDING, PADDING);
            g2.setTransform(transform);

            //draw axes
            drawArrow(g, 0, 0, 400, 0);
            drawArrow(g, 0, 0, 0, 200);

//            transform.translate(world.getMinX(), world.getMinY());
//            transform.scale(getWidth() / world.getWidth(),
//                            getHeight() / world.getHeight());
            g2.setTransform(transform);

//            g2.draw(new Rectangle2D.Double(10, 20, 200, 20));
            g2.draw(new Rectangle2D.Double(world.getMinX(), world.getMinY(),
                                            world.getWidth(), world.getHeight()));

            world.getEntities().stream()
                .map(entity -> entity.getState().getTransformedShape())
                    .forEach(g2::draw);

            g2.setTransform(savedTrans);
        }

        private void drawArrow(Graphics g1, int x1, int y1, int x2, int y2) {
            final int ARR_SIZE = 4;

            Graphics2D g = (Graphics2D) g1.create();

            double dx = x2 - x1, dy = y2 - y1;
            double angle = Math.atan2(dy, dx);
            int len = (int) Math.sqrt(dx*dx + dy*dy);
            AffineTransform at = AffineTransform.getTranslateInstance(x1, y1);
            at.concatenate(AffineTransform.getRotateInstance(angle));
            g.transform(at);

            // Draw horizontal arrow starting in (0, 0)
            g.drawLine(0, 0, len, 0);
            g.fillPolygon(new int[]{len, len - ARR_SIZE, len - ARR_SIZE, len},
                    new int[]{0, -ARR_SIZE, ARR_SIZE, 0}, 4);
        }
    }
}
