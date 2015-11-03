package irl.fw.engine.world;

import irl.fw.shared.bodies.Body;
import irl.fw.shared.bodies.PhysicalState;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/1/15
 */
class BodyInstance {

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
