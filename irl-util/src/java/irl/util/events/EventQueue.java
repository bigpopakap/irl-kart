package irl.util.events;

import rx.Observable;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 10/31/15
 */
public class EventQueue<T> {

    //TODO IMPORTANT!
    // This class should handle buffering all the events
    // so that none of them get lost
    private Observable<T> eventQueue;

    public EventQueue() {
        eventQueue = Observable.never();
    }

    public Observable<T> getQueue() {
        return eventQueue;
    }

    public EventQueue<T> merge(EventQueue<? extends T> queue) {
        return merge(queue.getQueue());
    }

    public EventQueue<T> merge(Observable<? extends T> events) {
        eventQueue = eventQueue.mergeWith(events);
        return this;
    }

    public EventQueue<T> queue(T event) {
        return merge(Observable.from((T[]) new Object[]{event}));
    }

}
