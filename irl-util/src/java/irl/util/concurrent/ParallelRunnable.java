package irl.util.concurrent;

import irl.util.callbacks.Callback;
import irl.util.callbacks.Callbacks;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/3/15
 */
public class ParallelRunnable implements StoppableRunnable {

    private final StoppableRunnable[] runnables;
    private volatile boolean isStopped = true;
    private final Callbacks onStop;

    public ParallelRunnable(boolean stopAllTogether, StoppableRunnable... runnables) {
        this.runnables = runnables;
        onStop = new Callbacks();

        //stop all processes when one of them stops
        if (stopAllTogether) {
            for (StoppableRunnable runnable : runnables) {
                runnable.onStop(this::stop);
            }
        }
    }

    @Override
    public synchronized void stop() {
        if (!isStopped()) {
            for (StoppableRunnable runnable : runnables) {
                runnable.stop();
            }

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

    @Override
    public void run() {
        isStopped = false;

        ExecutorService exec = Executors.newCachedThreadPool();
        for (StoppableRunnable runnable : runnables) {
            exec.execute(runnable);
        }
    }

}
