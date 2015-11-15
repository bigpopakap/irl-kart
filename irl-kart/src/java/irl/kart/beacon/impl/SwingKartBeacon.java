package irl.kart.beacon.impl;

import irl.kart.beacon.KartBeacon;
import irl.kart.beacon.KartBeaconEvent;
import irl.kart.events.kart.KartEvent;
import irl.kart.events.kart.SpinKart;
import irl.util.callbacks.Callback;
import irl.util.callbacks.Callbacks;
import irl.util.concurrent.StoppableRunnable;
import irl.util.reactiveio.EventQueue;
import irl.util.universe.Universe;
import rx.Observable;

import javax.swing.*;
import java.util.Optional;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/14/15
 */
public class SwingKartBeacon implements KartBeacon, StoppableRunnable {

    private volatile boolean isStopped = true;
    private final Callbacks onStop;

    private final JPanel panel;
    private final SwingKeyListener keyListener;
    private final EventQueue<KartBeaconEvent> updates;

    private final Universe<SwingKart> karts;

    public SwingKartBeacon(JPanel panel) {
        onStop = new Callbacks();

        this.karts = new Universe<>();
        this.karts.add("kart1", new SwingKartFactory(SwingKeyMapping.ARROWS));
        this.karts.add("kart2", new SwingKartFactory(SwingKeyMapping.WASD));
        this.karts.add("kart3", new SwingKartFactory(SwingKeyMapping.UHJK));

        this.panel = panel;
        this.keyListener = new SwingKeyListener();

        this.updates = new EventQueue<>();
        this.updates.mergeIn(keyListener.getKeys()
                .map(this::keyEventToBeaconEvent)
                .filter(update -> update != null));
    }

    @Override
    public Observable<KartBeaconEvent> stream() {
        return updates.get();
    }

    private KartBeaconEvent keyEventToBeaconEvent(SwingKeyEvent keyEvent) {
        //assuming no overlap in keys, just return the update from the first
        //kart that knows how to handle this key
        for (SwingKart kart : karts.toCollection()) {
            KartBeaconEvent beaconEvent = kart.handleKeyAndUpdate(keyEvent);
            if (beaconEvent != null) {
                return beaconEvent;
            }
        }

        //if no kart could handle the key, return null
        return null;
    }

    @Override
    public void send(KartEvent event) {
        if (event instanceof SpinKart) {
            SpinKart spinKart = ((SpinKart) event);
            Optional<SwingKart> kartToSpinOpt = karts.get(spinKart.getKartId());

            if (kartToSpinOpt.isPresent()) {
                SwingKart kartToSpin = kartToSpinOpt.get();
                kartToSpin.spin();
            }
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

}
