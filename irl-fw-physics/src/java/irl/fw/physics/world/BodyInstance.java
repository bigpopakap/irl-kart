package irl.fw.physics.world;

import irl.fw.physics.bodies.Body;
import rx.Observable;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 10/29/15
 */
class BodyInstance {

    private final Body body;
    private volatile PhysicalState state;

    BodyInstance(Body body) {
        this.body = body;
    }

    public Body getBody() {
        return body;
    }

    public void setState(PhysicalState state) {
        this.state = state;
    }

    public PhysicalState getState() {
        return state;
    }

}
