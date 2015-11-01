package irl.fw.physics.events;

import irl.fw.physics.world.PhysicalState;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 10/30/15
 */
public class UpdateBody implements PhysicalEvent {

    public interface AfterUpdateBody {
        void afterUpdateBody(boolean wasActuallyUpdated);
    }

    private final String bodyId;
    private final PhysicalState newState;
    private final AfterUpdateBody callback;

    public UpdateBody(String bodyId, PhysicalState newState) {
        this(bodyId, newState, (b) -> {});
    }

    public UpdateBody(String bodyId, PhysicalState newState, AfterUpdateBody callback) {
        this.bodyId = bodyId;
        this.newState = newState;
        this.callback = callback;
    }

    public String getBodyId() {
        return bodyId;
    }

    public PhysicalState getNewState() {
        return newState;
    }

    public void afterUpdateBody(boolean wasActuallyUpdated) {
        callback.afterUpdateBody(wasActuallyUpdated);
    }

}
