package irl.fw.physics.runner;

import irl.util.concurrent.RunAndStoppable;
import rx.Observable;
import rx.Observer;
import rx.schedulers.TimeInterval;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 10/31/15
 */
public class Simulator<T> implements RunAndStoppable {

    private static final long TIME_STEP = 33; //roughly 30fps

    private final Simulatable<T> simulatable;
    private volatile Observable<T> eventQueue;
    private boolean isPrepared;

    public Simulator(Simulatable<T> simulatable) {
        this.simulatable = simulatable;
        this.eventQueue = Observable.never();
        isPrepared = false;
    }

    @Override
    public void stop() {
        //TODO
    }

    @Override
    public void pause() {
        //TODO
    }

    @Override
    public void resume() {
        //TODO
    }

    //TODO somehow get rid of the need for this stupid prepare() phase
    public void prepare() {
        simulatable.start(this::queue);
        isPrepared = true;
    }

    @Override
    public void run() {
        if (!isPrepared) {
            throw new RuntimeException("Make sure you call prepare() before running");
        }

        eventQueue
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
        return false; //TODO
    }

    @Override
    public boolean isPaused() {
        return false; //TODO
    }

    @SafeVarargs
    private final void queue(Observable<? extends T>... eventses) {
        for (Observable<? extends T> events : eventses) {
            eventQueue = eventQueue.mergeWith(events);
        }
    }

}
