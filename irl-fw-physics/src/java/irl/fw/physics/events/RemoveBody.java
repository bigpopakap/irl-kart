package irl.fw.physics.events;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 10/30/15
 */
public class RemoveBody implements PhysicalEvent {

    public interface AfterRemoveBody {
        void afterRemoveBody(boolean wasActuallyRemoved);
    }

    private final String bodyId;
    private final AfterRemoveBody callback;

    public RemoveBody(String bodyId) {
        this(bodyId, (b) -> {});
    }

    public RemoveBody(String bodyId, AfterRemoveBody callback) {
        this.bodyId = bodyId;
        this.callback = callback;
    }

    public String getBodyId() {
        return bodyId;
    }

    public void afterRemoveBody(boolean wasActuallyRemoved) {
        callback.afterRemoveBody(wasActuallyRemoved);
    }
}
