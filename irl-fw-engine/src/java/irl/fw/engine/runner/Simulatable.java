package irl.fw.engine.runner;

import irl.fw.graphics.Frame;
import irl.util.reactiveio.Pipe;

import java.util.concurrent.TimeUnit;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 10/31/15
 */
public interface Simulatable<T> {

    void start(Pipe<T> eventQueue);

    void handleEvent(T event);
    void updatePhysics(long timeStep);
    Frame render(long timeSinceLastUpdate, TimeUnit timeUnit);

}
