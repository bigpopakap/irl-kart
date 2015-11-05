package irl.util.concurrent;

import irl.util.callbacks.Callback;
import irl.util.callbacks.Callbacks;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/1/15
 */
public abstract class LoopingRunnable implements PauseableRunnable {

    private volatile boolean isStopped;
    private volatile boolean isPaused;
    private final Callbacks onStop;

    public LoopingRunnable() {
        isStopped = true;
        isPaused = false;
        onStop = new Callbacks();
    }

    protected void beforeLoop() { /* default to nothing */ };
    protected abstract void loopIteration();

    @Override
    public void run() {
        isStopped = false;
        isPaused = false;

        beforeLoop();

        while (!isStopped()) {
            if (!isPaused()) {
                loopIteration();
            }
        }
    }

    @Override
    public synchronized void stop() {
        if (!isStopped()) {
            isStopped = true;
            onStop.run();
        }
    }

    @Override
    public synchronized void pause() {
        isPaused = true;
    }

    @Override
    public synchronized void resume() {
        isPaused = false;
    }

    @Override
    public boolean isStopped() {
        return isStopped;
    }

    @Override
    public String onStop(Callback callback) {
        return onStop.add(callback);
    }

    @Override
    public boolean isPaused() {
        return isPaused;
    }
}
