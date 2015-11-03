package irl.fw.beacon.beacons;

import irl.fw.beacon.Beacon;
import irl.fw.beacon.BeaconUpdate;
import irl.util.concurrent.StoppableRunnable;
import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

import javax.swing.*;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/2/15
 */
public class ArrowKeyBeacon implements Beacon, StoppableRunnable {

    private final String kartId;
    private final Subject<BeaconUpdate, BeaconUpdate> positions;

    public ArrowKeyBeacon(String kartId) {
        this.kartId = kartId;
        this.positions = PublishSubject.<BeaconUpdate>create().toSerialized();
    }

    @Override
    public void stop() {
        //TODO
    }

    @Override
    public boolean isStopped() {
        return false; //TODO
    }

    @Override
    public void run() {
        JFrame frame = new JFrame();
        frame.setVisible(true);
        //TODO
    }

    @Override
    public Observable<BeaconUpdate> updates() {
        return positions;
    }
}
