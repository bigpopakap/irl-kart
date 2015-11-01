package irl.fw.physics.events;

import irl.fw.physics.bodies.Body;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 10/30/15
 */
public class AddBody implements PhysicalEvent {

    public interface AfterAddBody {
        void afterAddBody(String newBodyId);
    }

    private final Body body;
    private final AfterAddBody callback;

    public AddBody(Body body) {
        this(body, id -> {});
    }

    public AddBody(Body body, AfterAddBody callback) {
        this.body = body;
        this.callback = callback;
    }

    public Body getBody() {
        return body;
    }

    public void afterAddBody(String newBodyId) {
        callback.afterAddBody(newBodyId);
    }

}
