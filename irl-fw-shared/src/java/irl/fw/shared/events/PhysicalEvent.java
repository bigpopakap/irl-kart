package irl.fw.shared.events;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 10/30/15
 */
public interface PhysicalEvent {

    default String getName() {
        return getClass().getSimpleName();
    }

}
