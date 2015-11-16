package irl.fw.engine.events;

import irl.fw.engine.entity.joints.Joint;
import irl.fw.engine.entity.joints.factory.JointFactory;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/15/15
 */
public class AddJoint implements EngineEvent {

    private final JointFactory<? extends Joint> jointFactory;

    public AddJoint(JointFactory<? extends Joint> jointFactory) {
        this.jointFactory = jointFactory;
    }

    public JointFactory<? extends Joint> getJointFactory() {
        return jointFactory;
    }

}
