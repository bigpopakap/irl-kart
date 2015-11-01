package irl.fw.physics.world;

import irl.fw.physics.bodies.Body;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/1/15
 */
public class BodyInstance {

    private final Body body;
    private volatile PhysicalState state;

    public BodyInstance(Body body) {
        this.body = body;
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
