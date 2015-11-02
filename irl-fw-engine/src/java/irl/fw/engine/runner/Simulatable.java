package irl.fw.engine.runner;

import rx.Observable;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 10/31/15
 */
public interface Simulatable<T> {

    void start(Consumer<Observable<? extends T>> queueEvents);

    void handleEvent(T event);
    void updatePhysics(long timeStep);
    void render(long timeSinceLastUpdate, TimeUnit timeUnit);

}
