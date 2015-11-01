package irl.fw.physics.runner;

import irl.util.events.EventQueue;
import rx.Observer;

import java.util.concurrent.TimeUnit;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 10/31/15
 */
public abstract class EventQueueSimulatable<T> implements Simulatable, Observer<T> {

    private final EventQueue<T> eventQueue;

    public EventQueueSimulatable() {
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
