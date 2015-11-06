package irl.kart.world;

import irl.fw.engine.entity.state.EntityStateUpdate;
import irl.fw.engine.geometry.Vector2D;
import irl.kart.beacon.KartBeacon;
import irl.kart.beacon.KartUpdate;
import irl.fw.engine.graphics.Renderer;
import irl.fw.engine.entity.EntityInstance;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
    public void render(Collection<EntityInstance> entities, long timeSinceLastUpdate) {
        long now = System.currentTimeMillis();

        List<String> lines = new ArrayList<>();

        lines.add("World");
        lines.add(String.format("Updated at %s", (now - timeSinceLastUpdate)));
        lines.add(String.format("rendered %s millis later", timeSinceLastUpdate));

        for (EntityInstance entityInstance : entities) {
            lines.add(String.format("Entity %s", entityInstance.toString()));
            lines.add(String.format("State %s", entityInstance.getState()));
            lines.add("");
        }

        if (frame != null && panel != null) {
            panel.setContents(lines);
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
                    y += 20;
                }
            }
        }
    }
}
