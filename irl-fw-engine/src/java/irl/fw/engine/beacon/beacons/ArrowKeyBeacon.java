package irl.fw.engine.beacon.beacons;

import irl.fw.engine.beacon.Beacon;
import irl.fw.engine.beacon.BeaconUpdate;
import irl.fw.engine.bodies.PhysicalState;
import irl.util.concurrent.StoppableRunnable;
import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/2/15
 */
public class ArrowKeyBeacon implements Beacon, StoppableRunnable {

    private final String kart1Id;
    private final String kart2Id;

    private final Subject<KeyEvent, KeyEvent> positions;
    private volatile int kart1Position = 0;
    private volatile int kart2Position = 0;

    private volatile boolean isStopped = true;
    private JFrame frame;

    public ArrowKeyBeacon(String kart1Id, String kart2Id) {
        this.kart1Id = kart1Id;
        this.kart2Id = kart2Id;
        this.positions = PublishSubject.<KeyEvent>create().toSerialized();
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
        JPanel panel = new JPanel();
        frame.add(panel);

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        panel.setSize(400, 400);

        panel.setFocusable(true);
        panel.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                //do nothing
            }

            @Override
            public void keyPressed(KeyEvent e) {
                positions.onNext(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                //do nothing
            }
        });

        frame.setVisible(true);
        panel.setVisible(true);
        panel.requestFocusInWindow();
        isStopped = false;
    }

    @Override
    public Observable<BeaconUpdate> updates() {
        return positions
                .map(this::keyEventToUpdate)
                .filter(update -> update != null);
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
                        new PhysicalState(--kart1Position)
                );
            default:
                return null;
        }
    }

}
