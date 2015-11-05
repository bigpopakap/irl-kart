package irl.fw.engine.events;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 10/30/15
 */
public interface EngineEvent {

    default String getName() {
        return getClass().getSimpleName();
    }

}
