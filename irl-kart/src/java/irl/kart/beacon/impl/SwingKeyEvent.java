package irl.kart.beacon.impl;

import java.awt.event.KeyEvent;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/15/15
 */
class SwingKeyEvent {

    public enum Type {
        KEY_DOWN, KEY_UP
    }

    private final Type type;
    private final KeyEvent event;

    public SwingKeyEvent(Type type, KeyEvent event) {
        this.type = type;
        this.event = event;
    }

    public Type getType() {
        return type;
    }

    public KeyEvent getEvent() {
        return event;
    }

}
