package irl.fw.physics.world;

import irl.fw.physics.bodies.Body;

import javax.vecmath.Vector2d;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 10/29/15
 */
class BodyInstance {

    private final Body body;
    //TODO rotation, position, velocity, accel

    public BodyInstance(Body body) {
        this.body = body;
    }

    public Body getBody() {
        return body;
    }

}
