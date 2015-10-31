package irl.fw.physics.runner;

import irl.fw.physics.events.Event;
import rx.Observable;
import rx.Observer;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 10/31/15
 */
public interface Loopable extends Observer<Event> {

    default boolean isDone() {
        return false;
    }

    Observable<Event> eventQueue();
    void render(long timeSinceLastUpdate);

}
