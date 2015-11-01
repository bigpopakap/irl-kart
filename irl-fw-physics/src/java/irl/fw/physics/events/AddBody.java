package irl.fw.physics.events;

import irl.fw.physics.bodies.Body;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 10/30/15
 */
public class AddBody implements PhysicalEvent {

    private final Body body;


    public AddBody(Body body) {
        this.body = body;
    }

    public Body getBody() {
        return body;
    }

}
