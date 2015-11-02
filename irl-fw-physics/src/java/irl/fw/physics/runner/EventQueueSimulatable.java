package irl.fw.physics.runner;

import rx.Observable;
import rx.Observer;

import java.util.concurrent.TimeUnit;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 10/31/15
 */
public abstract class EventQueueSimulatable<T> implements Simulatable, Observer<T> {

    private Observable<T> eventQueue;

    public EventQueueSimulatable() {
        this.eventQueue = Observable.never();
    }

    @Override
    public void processInput(long timeStep, TimeUnit timeUnit) {
        //TODO handle input
    }

    @SafeVarargs
    protected final void queue(Observable<? extends T>... eventses) {
        for (Observable<? extends T> events : eventses) {
            eventQueue = eventQueue.mergeWith(events);
        }
    }

}
