package irl.kart.beacon.impl;

import irl.fw.engine.entity.state.EntityStateUpdate;
import irl.fw.engine.geometry.Angle;
import irl.fw.engine.geometry.Vector2D;
import irl.kart.beacon.KartBeacon;
import irl.kart.beacon.KartBeaconEvent;
import irl.kart.entities.Kart;
import irl.kart.events.beacon.KartStateUpdate;
import irl.kart.events.beacon.UseItem;
import irl.kart.events.kart.KartEvent;
import irl.kart.events.kart.SpinKart;
import irl.util.callbacks.Callback;
import irl.util.callbacks.Callbacks;
import irl.util.concurrent.StoppableRunnable;
import irl.util.reactiveio.Pipe;
import rx.Observable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/14/15
 */
public class SwingKartBeacon implements KartBeacon, StoppableRunnable {

    private static final String KART1_ID = "kart1";
    private static final String KART2_ID = "kart2";

    private volatile boolean isStopped = true;
    private final Callbacks onStop;

    private final JPanel panel;
    private final MyKeyListener keyListener;
    private final Pipe<KartBeaconEvent> updates;

    //pretending to be the "state" of the real world
    private volatile double kart1rot = 0;
    private volatile double kart1speed = 0;
    private volatile double kart2rot = 0;
    private volatile double kart2speed = 0;

    public SwingKartBeacon(JPanel panel) {
        onStop = new Callbacks();

        this.panel = panel;
        this.keyListener = new MyKeyListener();

        this.updates = new Pipe<>();
        this.updates.mergeIn(keyListener.getKeys()
                .map(this::keyEventToBeaconEvent)
                .filter(update -> update != null));
    }

    @Override
    public Observable<KartBeaconEvent> stream() {
        return updates.get();
    }

    @Override
    public void send(KartEvent event) {
        if (event instanceof SpinKart) {
            String kartToSpin = ((SpinKart) event).getKartId();

            int keyToPress_lr;
            int keyToPress_back;
            if (kartToSpin.equals(KART1_ID)) {
                keyToPress_lr = (Math.random() < 0.5) ? KeyEvent.VK_LEFT : KeyEvent.VK_RIGHT;
                keyToPress_back = KeyEvent.VK_DOWN;
            } else if (kartToSpin.equals(KART2_ID)) {
                keyToPress_lr = (Math.random() < 0.5) ? KeyEvent.VK_A : KeyEvent.VK_D;
                keyToPress_back = KeyEvent.VK_S;
            } else {
                return; //do nothing
            }

            new Thread(() -> {
                Robot robot;
                try {
                    robot = new Robot();
                } catch (AWTException e) {
                    e.printStackTrace();
                    return;
                }

                robot.setAutoDelay(10);
                for (int i = 0; i < 20; i++) {
                    robot.keyPress(keyToPress_lr);
                    robot.keyRelease(keyToPress_lr);
                    if (i < 5) {
                        robot.keyPress(keyToPress_back);
                        robot.keyRelease(keyToPress_back);
                    }
                }
            }).start();
        } else {
            System.err.println("Unhandled or unexpected event: " + event.getName());
        }
    }

    @Override
    public void run() {
        isStopped = false;
        panel.addKeyListener(keyListener);
    }

    @Override
    public void stop() {
        if (!isStopped()) {
            panel.removeKeyListener(keyListener);
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

    private KartBeaconEvent keyEventToBeaconEvent(KeyEvent evt) {
        switch (evt.getKeyCode()) {
            /* KART 1 speed */
            case KeyEvent.VK_UP:
                kart1speed = Math.min(Kart.MAX_SPEED, kart1speed + Kart.SPEED_INCR);
                EntityStateUpdate update1speedUp = new EntityStateUpdate()
                        .velocity(new Vector2D(0, kart1speed).rotate(Angle.deg(kart1rot)))
                        .rotation(Angle.deg(kart1rot));
                return new KartStateUpdate(KART1_ID, update1speedUp);
            case KeyEvent.VK_DOWN:
                kart1speed = Math.max(Kart.MIN_SPEED, kart1speed - Kart.SPEED_INCR);
                EntityStateUpdate update1speedDown = new EntityStateUpdate()
                        .velocity(new Vector2D(0, kart1speed).rotate(Angle.deg(kart1rot)))
                        .rotation(Angle.deg(kart1rot));
                return new KartStateUpdate(KART1_ID, update1speedDown);

            /* KART 1 rotation */
            case KeyEvent.VK_LEFT:
                kart1rot += Kart.ROT_INCR;
                EntityStateUpdate stateUpdateRight = new EntityStateUpdate()
                        .velocity(new Vector2D(0, kart1speed).rotate(Angle.deg(kart1rot)))
                        .rotation(Angle.deg(kart1rot));
                return new KartStateUpdate(KART1_ID, stateUpdateRight);
            case KeyEvent.VK_RIGHT:
                kart1rot -= Kart.ROT_INCR;
                EntityStateUpdate stateUpdateLeft = new EntityStateUpdate()
                        .velocity(new Vector2D(0, kart1speed).rotate(Angle.deg(kart1rot)))
                        .rotation(Angle.deg(kart1rot));
                return new KartStateUpdate(KART1_ID, stateUpdateLeft);

            /* KART 1 fire weapon */
            case KeyEvent.VK_ENTER:
                return new UseItem(KART1_ID);

            /* KART 2 speed */
            case KeyEvent.VK_W:
                kart2speed = Math.min(Kart.MAX_SPEED, kart2speed + Kart.SPEED_INCR);
                EntityStateUpdate update2speedUp = new EntityStateUpdate()
                        .velocity(new Vector2D(0, kart2speed).rotate(Angle.deg(kart2rot)))
                        .rotation(Angle.deg(kart2rot));
                return new KartStateUpdate(KART2_ID, update2speedUp);
            case KeyEvent.VK_S:
                kart2speed = Math.max(Kart.MIN_SPEED, kart2speed - Kart.SPEED_INCR);
                EntityStateUpdate update2speedDown = new EntityStateUpdate()
                        .velocity(new Vector2D(0, kart2speed).rotate(Angle.deg(kart2rot)))
                        .rotation(Angle.deg(kart2rot));
                return new KartStateUpdate(KART2_ID, update2speedDown);

            /* KART 2 rotation */
            case KeyEvent.VK_A:
                kart2rot += Kart.ROT_INCR;
                EntityStateUpdate stateUpdate2Right = new EntityStateUpdate()
                        .velocity(new Vector2D(0, kart2speed).rotate(Angle.deg(kart2rot)))
                        .rotation(Angle.deg(kart2rot));
                return new KartStateUpdate(KART2_ID, stateUpdate2Right);
            case KeyEvent.VK_D:
                kart2rot -= Kart.ROT_INCR;
                EntityStateUpdate stateUpdate2Left = new EntityStateUpdate()
                        .velocity(new Vector2D(0, kart2speed).rotate(Angle.deg(kart2rot)))
                        .rotation(Angle.deg(kart2rot));
                return new KartStateUpdate(KART2_ID, stateUpdate2Left);

            /* KART 2 fire weapon */
            case KeyEvent.VK_SPACE:
                return new UseItem(KART2_ID);

            default:
                return null;
        }
    }

}
