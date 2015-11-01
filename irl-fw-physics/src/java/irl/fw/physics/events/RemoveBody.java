package irl.fw.physics.events;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 10/30/15
 */
public class RemoveBody implements PhysicalEvent {

    private final String bodyId;

    public RemoveBody(String bodyId) {
        this.bodyId = bodyId;
    }

    public String getBodyId() {
        return bodyId;
    }

}
