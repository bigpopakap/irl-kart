package irl.util.callbacks;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/5/15
 */
public interface Callback extends Runnable {

    static Callback fromRunnable(Runnable r) {
        return () -> r.run();
    }

    //nothing special for callbacks

}
