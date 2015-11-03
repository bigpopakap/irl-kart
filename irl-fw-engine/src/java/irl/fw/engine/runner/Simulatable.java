package irl.fw.engine.runner;

import irl.util.reactiveio.Pipe;

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
    void render(long timeSinceLastUpdate);

}
