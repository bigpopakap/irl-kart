package irl.fw.engine.physics.impl.dyn4j;

import irl.fw.engine.entity.joints.JointPoint;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.joint.DistanceJoint;
import org.dyn4j.dynamics.joint.Joint;
import org.dyn4j.geometry.Vector2;

import static irl.fw.engine.physics.impl.dyn4j.Dyn4jConverter.*;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/15/15
 */
public class Dyn4jJointConverter {

    private final Dyn4jEntityConverter entityConverter;

    public Dyn4jJointConverter(Dyn4jEntityConverter entityConverter) {
        this.entityConverter = entityConverter;
    }

    public Joint fromJoint(irl.fw.engine.entity.joints.factory.JointFactory jointFactory) {
        if (irl.fw.engine.entity.joints.DistanceJoint.class.isAssignableFrom(jointFactory.getType())) {
            return fromDistanceJoint(jointFactory);
        } else {
            throw new UnsupportedOperationException("Unsupported joint type: " + jointFactory.getType());
        }
    }

    private DistanceJoint fromDistanceJoint(irl.fw.engine.entity.joints.factory.JointFactory<irl.fw.engine.entity.joints.DistanceJoint> distanceJointFactory) {
        JointPoint jp1 = distanceJointFactory.getPoint1();
        JointPoint jp2 = distanceJointFactory.getPoint2();

        Body body1 = entityConverter.fromEntity(jp1.getEntity()).get();
        Body body2 = entityConverter.fromEntity(jp2.getEntity()).get();

        Vector2 point1 = fromVector(jp1.getLocation());
        Vector2 point2 = fromVector(jp2.getLocation());

        return new DistanceJoint(body1, body2, point1, point2);
    }

}
