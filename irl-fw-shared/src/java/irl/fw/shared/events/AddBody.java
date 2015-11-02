package irl.fw.shared.events;

import irl.fw.shared.bodies.Body;

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
