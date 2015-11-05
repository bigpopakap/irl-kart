package irl.kart.world;

import irl.fw.engine.beacon.Beacon;
import irl.fw.engine.beacon.BeaconUpdate;
import irl.fw.engine.bodies.PhysicalState;
import irl.fw.engine.graphics.Renderer;
import irl.fw.engine.physics.BodyInstance;
import irl.util.concurrent.StoppableRunnable;
import irl.util.reactiveio.Pipe;
import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/2/15
 */
public class SwingWorld implements Beacon, Renderer, StoppableRunnable {

    private final String kart1Id;
    private final String kart2Id;

    private final Subject<KeyEvent, KeyEvent> rawPositions;
    private final Pipe<BeaconUpdate> updates;
    private volatile Integer kart1Position = 0;
    private volatile Integer kart2Position = 0;

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
    }

    @Override
    public void stop() {
        frame.dispose();
        isStopped = true;
    }

    @Override
    public boolean isStopped() {
        return isStopped;
    }

    @Override
    public void run() {
        this.frame = new JFrame();
        this.panel = new MyPanel();
        frame.add(panel);

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(800, 400);
        panel.setSize(800, 400);

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

        frame.setVisible(true);
        panel.setVisible(true);
        panel.requestFocusInWindow();
    }

    @Override
    public Observable<BeaconUpdate> updates() {
        return updates.get();
    }

    @Override
    public void render(Collection<BodyInstance> bodies, long timeSinceLastUpdate) {
        long now = System.currentTimeMillis();

        List<String> lines = new ArrayList<>();

        lines.add("World");
        lines.add(String.format("Updated at %s", (now - timeSinceLastUpdate)));
        lines.add(String.format("rendered %s millis later\n", timeSinceLastUpdate));

        for (BodyInstance bodyInstance : bodies) {
            lines.add(String.format("Body %s in state %s\n", bodyInstance.toString(), bodyInstance.getState()));
        }

        if (frame != null && panel != null) {
            panel.setContents(lines);
            frame.repaint();
        }
    }

    private BeaconUpdate keyEventToUpdate(KeyEvent evt) {
        switch (evt.getKeyCode()) {
            case KeyEvent.VK_UP:
                return new BeaconUpdate(
                    kart1Id,
                    new PhysicalState(++kart1Position)
                );
            case KeyEvent.VK_DOWN:
                return new BeaconUpdate(
                    kart1Id,
                    new PhysicalState(--kart1Position)
                );
            case KeyEvent.VK_W:
                return new BeaconUpdate(
                    kart2Id,
                    new PhysicalState(++kart2Position)
                );
            case KeyEvent.VK_S:
                return new BeaconUpdate(
                    kart2Id,
                    new PhysicalState(--kart2Position)
                );
            default:
                return null;
        }
    }

    private static class MyPanel extends JPanel {

        private volatile List<String> contents;

        public synchronized void setContents(List<String> contents) {
            this.contents = contents;
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);

            if (contents != null) {
                int y = 25;
                for (String content : contents) {
                    g.drawString(content, 25, y);
                    y += 10;
                }
            }
        }
    }
}
