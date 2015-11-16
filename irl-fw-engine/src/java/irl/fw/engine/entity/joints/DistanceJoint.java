package irl.fw.engine.entity.joints;

import irl.fw.engine.entity.joints.factory.JointConfig;
import irl.fw.engine.events.EngineEvent;
import irl.util.reactiveio.EventQueue;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/15/15
 */
public class DistanceJoint extends Joint {

    public DistanceJoint(JointConfig jointConfig, EventQueue<EngineEvent> eventQueue) {
        super(jointConfig, eventQueue);
    }

}
