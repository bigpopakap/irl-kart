package irl.kart.beacon.impl;

import irl.fw.engine.entity.state.EntityStateBuilder;
import irl.fw.engine.events.AddEntity;
import irl.fw.engine.geometry.Angle;
import irl.fw.engine.geometry.Vector2D;
import irl.kart.beacon.KartBeacon;
import irl.kart.beacon.KartBeaconEvent;
import irl.kart.entities.Kart;
import irl.kart.events.beacon.KartStateUpdate;
import irl.kart.events.kart.KartEvent;
import irl.kart.events.kart.SpinKart;
import irl.util.callbacks.Callback;
import irl.util.concurrent.StoppableRunnable;
import rx.Observable;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/14/15
 */
public class SwingKartBeacon implements KartBeacon, StoppableRunnable {

    @Override
    public Observable<KartBeaconEvent> stream() {
//        return updates.get();
    }

    @Override
    public void send(KartEvent event) {

    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isStopped() {
        return false;
    }

    @Override
    public String onStop(Callback callback) {
        return null;
    }

    @Override
    public void run() {
//        eventQueue.mergeIn(addNewKarts());
    }

    private Observable<AddEntity> addNewKarts() {
        return stream()
            .ofType(KartStateUpdate.class)
            .distinct(update -> update.getKartId())
            .map(update -> new AddEntity(entityConfig -> new Kart(
                entityConfig,
                new EntityStateBuilder()
                        .shape(Kart.SHAPE)
                        .rotation(Angle.deg(0))
                        .center(new Vector2D(200, 200))
                        .velocity(new Vector2D(0, 0))
                        .angularVelocity(Angle.deg(0))
                        .build(),
                update.getKartId(), this, eventQueue
            )));
    }

    @Override
    public void send(KartEvent event) {
        if (event instanceof SpinKart) {
            String kartToSpin = ((SpinKart) event).getKartId();

            int keyToPress_lr;
            int keyToPress_back;
            if (kartToSpin.equals(kart1Id)) {
                keyToPress_lr = (Math.random() < 0.5) ? KeyEvent.VK_LEFT : KeyEvent.VK_RIGHT;
                keyToPress_back = KeyEvent.VK_DOWN;
            } else if (kartToSpin.equals(kart2Id)) {
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

}
