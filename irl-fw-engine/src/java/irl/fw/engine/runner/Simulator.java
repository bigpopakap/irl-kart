package irl.fw.engine.runner;

import irl.util.concurrent.StoppableRunnable;
import irl.util.reactiveio.Pipe;
import rx.Observer;
import rx.Subscription;
import rx.schedulers.TimeInterval;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 10/31/15
 */
public class Simulator<T> implements StoppableRunnable {

    private static final long TIME_STEP = 33; //roughly 30fps

    private final Simulatable<T> simulatable;
    private volatile Pipe<T> eventQueue;
    private volatile Subscription subscription;

    public Simulator(Simulatable<T> simulatable) {
        this.simulatable = simulatable;
        this.eventQueue = new Pipe<>();
    }

    @Override
    public void stop() {
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }

    @Override
    public boolean isStopped() {
        return subscription == null || subscription.isUnsubscribed();
    }

    public Pipe<T> getEventQueue() {
        return eventQueue;
    }

    @Override
    public void run() {
        simulatable.start(eventQueue);

        this.subscription = eventQueue.get()
            .buffer(TIME_STEP, TimeUnit.MILLISECONDS)
            .timeInterval()
            .subscribe(new Observer<TimeInterval<List<T>>>() {

                private volatile long lag = 0;

                @Override
                public void onCompleted() {
                    //do nothing
                }

                @Override
                public void onError(Throwable e) {
                    e.printStackTrace();
                }

                @Override
                public void onNext(TimeInterval<List<T>> eventBatch) {
                    long elapsed = eventBatch.getIntervalInMilliseconds();
                    lag += elapsed;

                    //process the batched inputs
                    //TODO is there a way to do this with observables instead of just iterating?
                    eventBatch.getValue().forEach(simulatable::handleEvent);

                    //TODO is there a way to do this more function-oriented?
                    while (lag > TIME_STEP) {
                        simulatable.updatePhysics(TIME_STEP);
                        lag -= TIME_STEP;
                    }

                    simulatable.render(lag / TIME_STEP, TimeUnit.MILLISECONDS);
                }

            });
    }

}
