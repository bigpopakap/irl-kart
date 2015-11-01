package irl.util.loop;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/1/15
 */
public abstract class Loop implements Runnable {

    private volatile boolean isStopped;
    private volatile boolean isPaused;

    public Loop() {
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

    public synchronized void stop() {
        isStopped = true;
    }

    public synchronized void pause() {
        isPaused = true;
    }

    public synchronized void resume() {
        isPaused = false;
    }

    public boolean isStopped() {
        return isStopped;
    }

    public boolean isPaused() {
        return isPaused;
    }
}
