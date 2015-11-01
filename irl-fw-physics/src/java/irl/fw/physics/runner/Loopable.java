package irl.fw.physics.runner;

import java.util.concurrent.TimeUnit;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 10/31/15
 */
public interface Loopable {

    default boolean isDone() {
        return false;
    }

    void processInput(long timeStep, TimeUnit timeUnit);
    void updatePhysics(long timeStep, TimeUnit timeUnit);
    void render(long timeSinceLastUpdate, TimeUnit timeUnit);

}
