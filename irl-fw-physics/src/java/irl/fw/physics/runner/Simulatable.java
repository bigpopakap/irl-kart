package irl.fw.physics.runner;

import java.util.concurrent.TimeUnit;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 10/31/15
 */
public interface Simulatable {

    default boolean isDone() {
        return false;
    }

    void setTiming(long timeStep, TimeUnit timeUnit);

    void processInput();
    void updatePhysics();
    void render(long timeSinceLastUpdate);

}
