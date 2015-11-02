package irl.util.concurrent;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/1/15
 */
public interface PauseableRunnable extends StoppableRunnable {

    void pause();
    void resume();
    boolean isPaused();

}
