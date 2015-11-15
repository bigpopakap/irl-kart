package irl.util.concurrent;

import irl.util.callbacks.Callback;
import irl.util.callbacks.Callbacks;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/15/15
 */
public abstract  class SynchronousRunnable implements StoppableRunnable {

    private boolean isStopped = true;
    private final Callbacks onStop;

    public SynchronousRunnable() {
        onStop = new Callbacks();
    }

    protected abstract void doRunSynchronous();

    @Override
    public void run() {
        isStopped = false;
        doRunSynchronous();
        isStopped = true;
        onStop.run();
    }

    @Override
    public void stop() {
        //do nothing... this is a synchronous runnable
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
