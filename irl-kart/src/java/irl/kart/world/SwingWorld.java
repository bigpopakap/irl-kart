package irl.kart.world;

import irl.fw.engine.entity.state.EntityStateUpdate;
import irl.fw.engine.geometry.Angle;
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

    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;

    private final String kart1Id;
    private final String kart2Id;

    private final Callbacks onStop;

    private final Subject<KeyEvent, KeyEvent> rawPositions;
    private final Pipe<KartUpdate> updates;

    //pretending to be the "state" of the real world
    private static final double MAX_SPEED = 90;
    private static final double MIN_SPEED = -45;
    private static final double SPEED_INCR = 20;
    private static final double ROT_INCR = 15;
    private volatile double kart1rot = 0;
    private volatile double kart1speed = 0;
    private volatile double kart2rot = 180;
    private volatile double kart2speed = 0;

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
        panel.addKeyListener(new MyKeyListener());

        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        panel.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
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
            /* KART 1 speed */
            case KeyEvent.VK_UP:
                kart1speed = Math.min(MAX_SPEED, kart1speed + SPEED_INCR);
                EntityStateUpdate update1speedUp = new EntityStateUpdate()
                        .velocity(new Vector2D(0, kart1speed).rotate(Angle.deg(kart1rot)));
                return new KartUpdate(kart1Id, update1speedUp);
            case KeyEvent.VK_DOWN:
                kart1speed = Math.max(MIN_SPEED, kart1speed - SPEED_INCR);
                EntityStateUpdate update1speedDown = new EntityStateUpdate()
                        .velocity(new Vector2D(0, kart1speed).rotate(Angle.deg(kart1rot)));
                return new KartUpdate(kart1Id, update1speedDown);

            /* KART 1 rotation */
            case KeyEvent.VK_LEFT:
                kart1rot += ROT_INCR;
                EntityStateUpdate stateUpdateRight = new EntityStateUpdate()
                        .rotation(Angle.deg(kart1rot));
                return new KartUpdate(kart1Id, stateUpdateRight);
            case KeyEvent.VK_RIGHT:
                kart1rot -= ROT_INCR;
                EntityStateUpdate stateUpdateLeft = new EntityStateUpdate()
                        .rotation(Angle.deg(kart1rot));
                return new KartUpdate(kart1Id, stateUpdateLeft);

            /* KART 2 speed */
            case KeyEvent.VK_W:
                kart2speed = Math.min(MAX_SPEED, kart2speed + SPEED_INCR);
                EntityStateUpdate update2speedUp = new EntityStateUpdate()
                        .velocity(new Vector2D(0, kart2speed).rotate(Angle.deg(kart2rot)));
                return new KartUpdate(kart2Id, update2speedUp);
            case KeyEvent.VK_S:
                kart2speed = Math.max(MAX_SPEED, kart2speed - SPEED_INCR);
                EntityStateUpdate update2speedDown = new EntityStateUpdate()
                        .velocity(new Vector2D(0, kart2speed).rotate(Angle.deg(kart2rot)));
                return new KartUpdate(kart2Id, update2speedDown);

            /* KART 2 rotation */
            case KeyEvent.VK_A:
                kart2rot += ROT_INCR;
                EntityStateUpdate stateUpdate2Right = new EntityStateUpdate()
                        .rotation(Angle.deg(kart2rot));
                return new KartUpdate(kart2Id, stateUpdate2Right);
            case KeyEvent.VK_D:
                kart2rot -= ROT_INCR;
                EntityStateUpdate stateUpdate2Left = new EntityStateUpdate()
                        .rotation(Angle.deg(kart2rot));
                return new KartUpdate(kart2Id, stateUpdate2Left);

            default:
                return null;
        }
    }

    private static class MyPanel extends JPanel {

        public static final long serialVersionUID = 1L;

        private volatile World world;

        public synchronized void update(World world) {
            this.world = world;
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
                transform.scale((getWidth() - 2.0*PADDING) / getWidth(),
                                (getHeight() - 2.0*PADDING) / getHeight());
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
                    .forEach(g2::draw);

            //revert back to the original transform
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

    private class MyKeyListener implements KeyListener {

        private final Subject<KeyEvent, KeyEvent> keyPresses;
        private final Subject<KeyEvent, KeyEvent> keyReleases;

        public MyKeyListener() {
            keyPresses = PublishSubject.<KeyEvent>create().toSerialized();
            keyReleases = PublishSubject.<KeyEvent>create().toSerialized();

            keyPresses
                .takeUntil(keyReleases)
                .repeat()
                .subscribe(rawPositions);
        }

        @Override
        public void keyTyped(KeyEvent e) {
            //do nothing
        }

        @Override
        public void keyPressed(KeyEvent e) {
            keyPresses.onNext(e);
        }

        @Override
        public void keyReleased(KeyEvent e) {
            keyReleases.onNext(e);
        }

    }

}
