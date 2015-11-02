package irl.util.concurrent;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/1/15
 */
public interface StoppableRunnable extends Runnable {

    void stop();
    boolean isStopped();

}
