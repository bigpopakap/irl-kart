package irl.util.callbacks;

import irl.util.universe.Universe;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/5/15
 */
public class Callbacks implements Callback {

    private final Universe<Callback> callbacks;

    public Callbacks() {
        callbacks = new Universe<>();
    }

    public String add(Callback callback) {
        if (callback != null) {
            return callbacks.add(callback);
        } else {
            return null;
        }
    }

    public void remove(String callbackId) {
        callbacks.remove(callbackId);
    }

    @Override
    public void run() {
        callbacks.toCollection().forEach(callback -> callback.run());
    }

}
