package irl.fw.engine.entity.joints.factory;

import irl.fw.engine.entity.joints.DistanceJoint;
import irl.fw.engine.entity.joints.JointPoint;
import irl.fw.engine.events.EngineEvent;
import irl.util.reactiveio.EventQueue;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/15/15
 */
public class DistanceJointFactory implements JointFactory<DistanceJoint> {

    private final EventQueue<EngineEvent> eventQueue;
    private final JointPoint point1;
    private final JointPoint point2;

    public DistanceJointFactory(JointPoint point1, JointPoint point2,
                                EventQueue<EngineEvent> eventQueue) {
        this.point1 = point1;
        this.point2 = point2;
        this.eventQueue = eventQueue;
    }

    @Override
    public Class<DistanceJoint> getType() {
        return DistanceJoint.class;
    }

    @Override
    public JointPoint getPoint1() {
        return point1;
    }

    @Override
    public JointPoint getPoint2() {
        return point2;
    }

    @Override
    public DistanceJoint create(JointConfig jointConfig) {
        return new DistanceJoint(jointConfig, eventQueue);
    }

}
