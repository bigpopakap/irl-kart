package irl.fw.shared.events;

import irl.fw.shared.bodies.Body;
import irl.fw.shared.bodies.PhysicalState;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 10/30/15
 */
public class AddBody implements PhysicalEvent {

    private final Body body;
    private final PhysicalState initialState;

    public AddBody(Body body, PhysicalState initialState) {
        this.body = body;
        this.initialState = initialState;
    }

    public Body getBody() {
        return body;
    }

    public PhysicalState getInitialState() {
        return initialState;
    }

}
