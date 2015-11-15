package irl.kart.beacon.impl;

import irl.kart.beacon.KartBeacon;
import irl.kart.beacon.KartBeaconEvent;
import irl.kart.events.kart.KartEvent;
import irl.kart.events.kart.SpinKart;
import irl.util.callbacks.Callback;
import irl.util.callbacks.Callbacks;
import irl.util.concurrent.StoppableRunnable;
import irl.util.reactiveio.Pipe;
import rx.Observable;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
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
    private final Pipe<KartBeaconEvent> updates;

    private final List<SwingKart> karts;

    public SwingKartBeacon(JPanel panel) throws Exception {
        onStop = new Callbacks();

        this.karts = new ArrayList<>();
        this.karts.add(new SwingKart("kart1", SwingKeyMapping.ARROWS_ENTER));
        this.karts.add(new SwingKart("kart2", SwingKeyMapping.WASD_SPACE));

        this.panel = panel;
        this.keyListener = new SwingKeyListener();

        this.updates = new Pipe<>();
        this.updates.mergeIn(keyListener.getKeys()
                .map(this::keyEventToBeaconEvent)
                .filter(update -> update != null));
    }

    @Override
    public Observable<KartBeaconEvent> stream() {
        return updates.get();
    }

    private KartBeaconEvent keyEventToBeaconEvent(KeyEvent keyEvent) {
        //assuming no overlap in keys, just return the update from the first
        //kart that knows how to handle this key
        for (SwingKart kart : karts) {
            KartBeaconEvent beaconEvent = kart.handleKeyAndUpdate(keyEvent.getKeyCode());
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
            Optional<SwingKart> kartToSpinOpt = getKart(spinKart.getKartId());

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

    private Optional<SwingKart> getKart(String id) {
        return karts.parallelStream()
            .filter(kart -> kart.getId().equals(id))
            .findFirst();
    }

}
