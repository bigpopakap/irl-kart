package irl.fw.physics.runner;

import irl.util.concurrent.StoppableRunnable;
import rx.Observable;
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
    private volatile Observable<T> eventQueue;
    private volatile Subscription subscription;
    private boolean isPrepared;

    public Simulator(Simulatable<T> simulatable) {
        this.simulatable = simulatable;
        isPrepared = false;
    }

    @Override
    public void stop() {
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }

    //FIXME somehow get rid of the need for this stupid prepare() phase
    public void prepare() {
        this.eventQueue = Observable.never();
        simulatable.start(this::queue);
        isPrepared = true;
    }

    @Override
    public void run() {
        if (!isPrepared) {
            throw new RuntimeException("Make sure you call prepare() before running");
        }

        this.subscription = eventQueue
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
                    eventBatch.getValue().forEach(simulatable::handleEvent);

                    //TODO is there a way to do this more function-oriented?
                    while (lag > TIME_STEP) {
                        simulatable.updatePhysics();
                        lag -= TIME_STEP;
                    }

                    simulatable.render(lag / TIME_STEP, TimeUnit.MILLISECONDS);
                }

            });
    }

    @Override
    public boolean isStopped() {
        return subscription == null || subscription.isUnsubscribed();
    }

    @SafeVarargs
    private final void queue(Observable<? extends T>... eventses) {
        for (Observable<? extends T> events : eventses) {
            eventQueue = eventQueue.mergeWith(events);
        }
    }

}
