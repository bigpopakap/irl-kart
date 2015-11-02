package irl.util.concurrent;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/1/15
 */
public abstract class Looper implements RunAndStoppable {

    private volatile boolean isStopped;
    private volatile boolean isPaused;

    public Looper() {
        isStopped = true;
        isPaused = false;
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
        isStopped = true;
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
    public boolean isPaused() {
        return isPaused;
    }
}
