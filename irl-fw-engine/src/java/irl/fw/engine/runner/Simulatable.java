package irl.fw.engine.runner;

import rx.Observable;
import rx.Observer;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 10/31/15
 */
public interface Simulatable<T> extends Observer<T> {

    void start(Consumer<Observable<? extends T>> queueEvents);

    @Override
    default void onCompleted() {
        //do nothing
    }

    void updatePhysics(long timeStep);
    void render(long timeSinceLastUpdate, TimeUnit timeUnit);

}
