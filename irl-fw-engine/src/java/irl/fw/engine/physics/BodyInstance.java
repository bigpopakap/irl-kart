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
    private final PhysicalState state;

    public BodyInstance(Body body, PhysicalState state) {
        this.body = body;
        this.state = state;
    }

    public Body getBody() {
        return body;
    }

    public PhysicalState getState() {
        return state;
    }

    public BodyInstance setState(PhysicalState state) {
        return new BodyInstance(body, state);
    }

}
