package irl.fw.physics.runner;

import rx.Observer;

import java.util.concurrent.TimeUnit;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 10/31/15
 */
public abstract class EventQueueLoopable<T> implements Loopable, Observer<T> {

    private final EventQueue<T> eventQueue;

    public EventQueueLoopable() {
        this.eventQueue = new EventQueue<>();
    }

    @Override
    public void processInput(long timeStep, TimeUnit timeUnit) {
        eventQueue.getQueue()
                .take(timeStep, timeUnit)
                .subscribe(this);
    }

    protected EventQueue<T> getEventQueue() {
        return eventQueue;
    }

}
