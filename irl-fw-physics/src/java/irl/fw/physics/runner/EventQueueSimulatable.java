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

    private long timeStep;
    private TimeUnit timeUnit;

    private Observable<T> eventQueue;

    public EventQueueSimulatable() {
        this.eventQueue = Observable.never();
    }

    @Override
    public void setTiming(long timeStep, TimeUnit timeUnit) {
        this.timeStep = timeStep;
        this.timeUnit = timeUnit;
    }

    @Override
    public void processInput() {
        eventQueue
            .window(getTimeStep(), getTimeUnit())
            .forEach(window -> window.subscribe(this));
    }

    protected long getTimeStep() {
        return timeStep;
    }

    protected TimeUnit getTimeUnit() {
        return timeUnit;
    }

    @SafeVarargs
    protected final void queue(Observable<? extends T>... eventses) {
        for (Observable<? extends T> events : eventses) {
            eventQueue = eventQueue.mergeWith(events);
        }
    }

}
