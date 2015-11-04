package irl.util.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/3/15
 */
public class ParallelRunnable implements StoppableRunnable {

    private final long timeout;
    private final TimeUnit timeoutUnit;
    private final StoppableRunnable main;
    private final StoppableRunnable[] others;
    private volatile boolean isStopped = true;

    public ParallelRunnable(StoppableRunnable main, StoppableRunnable[] others,
                            long timeout, TimeUnit timeoutUnit) {
        this.timeout = timeout;
        this.timeoutUnit = timeoutUnit;
        this.main = main;
        this.others = others != null ? others : new StoppableRunnable[0];
    }

    @Override
    public void stop() {
        main.stop();

        for (StoppableRunnable runnable : others) {
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

        ExecutorService mainExec = Executors.newSingleThreadExecutor();
        ExecutorService otherExec = Executors.newCachedThreadPool();

        for (StoppableRunnable runnable : others) {
            otherExec.execute(runnable);
        }
        mainExec.execute(main);

        //now wait for main to complete before killing the others
        try {
            mainExec.awaitTermination(timeout, timeoutUnit);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //FIXME this doesn't actually stop the other threads
        //when awaitTermintation() times out
        stop();
    }

}
