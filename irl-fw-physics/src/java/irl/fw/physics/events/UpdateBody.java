package irl.fw.physics.events;

import irl.fw.physics.world.PhysicalState;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 10/30/15
 */
public class UpdateBody implements PhysicalEvent {

    private final String bodyId;
    private final PhysicalState newState;

    public UpdateBody(String bodyId, PhysicalState newState) {
        this.bodyId = bodyId;
        this.newState = newState;
    }

    public String getBodyId() {
        return bodyId;
    }

    public PhysicalState getNewState() {
        return newState;
    }

}
