package irl.util.loop;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/1/15
 */
public interface Looper extends Runnable {

    void stop();
    void pause();
    void resume();

    boolean isStopped();
    boolean isPaused();

}
