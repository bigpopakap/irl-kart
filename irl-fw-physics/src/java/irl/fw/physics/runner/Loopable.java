package irl.fw.physics.runner;

import rx.Observable;
import rx.Observer;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 10/31/15
 */
public interface Loopable<T> extends Observer<T> {

    default boolean isDone() {
        return false;
    }

    Observable<T> eventQueue();
    void render(long timeSinceLastUpdate);

}
