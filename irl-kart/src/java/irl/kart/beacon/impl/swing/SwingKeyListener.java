package irl.kart.beacon.impl.swing;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static irl.kart.beacon.impl.swing.SwingKeyEvent.Type.*;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/14/15
 */
class SwingKeyListener implements KeyListener {

    private final Subject<KeyEvent, KeyEvent> keyPresses;
    private final Subject<KeyEvent, KeyEvent> keyReleases;

    public SwingKeyListener() {
        keyPresses = PublishSubject.<KeyEvent>create().toSerialized();
        keyReleases = PublishSubject.<KeyEvent>create().toSerialized();
    }

    public Observable<SwingKeyEvent> getKeys() {
        return keyPresses.map(keyEvent -> new SwingKeyEvent(KEY_DOWN, keyEvent))
            .mergeWith(keyReleases.map(keyEvent -> new SwingKeyEvent(KEY_UP, keyEvent)));
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
