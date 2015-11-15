package irl.util.concurrent;

import irl.util.callbacks.Callback;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/15/15
 */
public class SequentialRunnable implements StoppableRunnable {

    private final StoppableRunnable[] runnables;
    private int curIdx;

    public SequentialRunnable(StoppableRunnable... runnables) {
        this.runnables = runnables;
        curIdx = -1;
    }

    @Override
    public void run() {
        runNext();
    }

    private void runNext() {
        curIdx++;
        if (curIdx < runnables.length) {
            StoppableRunnable cur = runnables[curIdx];
            cur.onStop(this::runNext);
            cur.run();
        } else {
            //we've come to the end
            stop();
        }
    }

    @Override
    public void stop() {
        if (!isStopped()) {
            if (curIdx < runnables.length) {
                runnables[curIdx].stop();
            }
            curIdx = -1;
        }
    }

    @Override
    public boolean isStopped() {
        return curIdx < 0;
    }

    @Override
    public String onStop(Callback callback) {
        return null;
    }

}
