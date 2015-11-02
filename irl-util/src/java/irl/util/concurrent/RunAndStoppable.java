package irl.util.concurrent;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/1/15
 */
public interface RunAndStoppable extends Runnable {

    void stop();
    void pause();
    void resume();

    boolean isStopped();
    boolean isPaused();

}
