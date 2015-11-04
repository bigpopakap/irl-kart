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

    private final String kartId;
    private final Subject<BeaconUpdate, BeaconUpdate> positions;

    private volatile boolean isStopped;
    private JFrame frame;

    public ArrowKeyBeacon(String kartId) {
        this.kartId = kartId;
        this.positions = PublishSubject.<BeaconUpdate>create().toSerialized();
        isStopped = true;
    }

    @Override
    public void stop() {
        frame.dispose();
        isStopped = true;
    }

    @Override
    public boolean isStopped() {
        return frame.isActive();
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
                BeaconUpdate update = new BeaconUpdate(
                        kartId,
                        new PhysicalState("" + e.getKeyChar())
                );

                synchronized (positions) {
                    positions.onNext(update);
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
                //do nothing
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
        return positions;
    }
}
