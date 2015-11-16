package irl.fw.engine.entity.joints.factory;

import irl.fw.engine.entity.factory.EntityConfig;
import irl.fw.engine.entity.joints.DistanceJoint;
import irl.fw.engine.entity.joints.JointPoint;
import irl.fw.engine.events.EngineEvent;
import irl.util.reactiveio.EventQueue;

import java.util.function.Consumer;

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
    private final Consumer<DistanceJoint> afterCreate;

    public DistanceJointFactory(JointPoint point1, JointPoint point2,
                                EventQueue<EngineEvent> eventQueue) {
        this(point1, point2, eventQueue, null);
    }

    public DistanceJointFactory(JointPoint point1, JointPoint point2,
                                EventQueue<EngineEvent> eventQueue,
                                Consumer<DistanceJoint> afterCreate) {
        this.point1 = point1;
        this.point2 = point2;
        this.eventQueue = eventQueue;
        this.afterCreate = afterCreate;
    }

    public JointPoint getPoint1() {
        return point1;
    }

    public JointPoint getPoint2() {
        return point2;
    }

    @Override
    public DistanceJoint create(EntityConfig entityConfig) {
        return create(new JointConfig()
                    .setId(entityConfig.getId())
                    .setPoint1(getPoint1())
                    .setPoint2(getPoint2()));
    }

    public DistanceJoint create(JointConfig jointConfig) {
        DistanceJoint joint = new DistanceJoint(jointConfig, eventQueue);
        if (afterCreate != null) {
            afterCreate.accept(joint);
        }
        return joint;
    }

}
