package irl.util.concurrent;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/3/15
 */
public class ParallelRunnable implements StoppableRunnable {

    private boolean isStopped;
    private final StoppableRunnable[] runnables;

    public ParallelRunnable(StoppableRunnable... runnables) {
        isStopped = true;
        this.runnables = runnables;
    }

    @Override
    public void stop() {
        for (StoppableRunnable runnable : runnables) {
            runnable.stop();
        }
        isStopped = true;
    }

    @Override
    public boolean isStopped() {
        return isStopped;
    }

    @Override
    public void run() {
        isStopped = false;

        for (StoppableRunnable runnable : runnables) {
            new Thread(runnable).start();
        }
    }

}
