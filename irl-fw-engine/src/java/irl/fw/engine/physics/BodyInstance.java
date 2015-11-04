package irl.fw.engine.physics;

import irl.fw.engine.bodies.Body;
import irl.fw.engine.bodies.PhysicalState;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/1/15
 */
public class BodyInstance {

    private final Body body;
    private volatile PhysicalState state;

    public BodyInstance(Body body, PhysicalState initialState) {
        this.body = body;
        setState(initialState);
    }

    public Body getBody() {
        return body;
    }

    public PhysicalState getState() {
        return state;
    }

    public synchronized void setState(PhysicalState state) {
        this.state = state;
    }

}
