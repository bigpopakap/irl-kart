package irl.fw.engine.physics.impl.dyn4j;

import irl.fw.engine.entity.factory.EntityConfig;
import irl.fw.engine.entity.joints.JointPoint;
import irl.fw.engine.entity.joints.factory.DistanceJointFactory;
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
        if (jointFactory instanceof DistanceJointFactory) {
            return fromDistanceJoint((DistanceJointFactory) jointFactory);
        } else {
            throw new UnsupportedOperationException("Unsupported joint factory type: " + jointFactory.getClass());
        }
    }

    private DistanceJoint fromDistanceJoint(DistanceJointFactory distanceJointFactory) {
        JointPoint jp1 = distanceJointFactory.getPoint1();
        JointPoint jp2 = distanceJointFactory.getPoint2();

        Body body1 = entityConverter.fromEntity(jp1.getEntity()).get();
        Body body2 = entityConverter.fromEntity(jp2.getEntity()).get();

        Vector2 point1 = fromVector(jp1.getLocation());
        Vector2 point2 = fromVector(jp2.getLocation());

        DistanceJoint dj = new DistanceJoint(body1, body2, point1, point2);
        distanceJointFactory.create(new EntityConfig()
                                            .setId(toId(dj.getId())));
        return dj;
    }

}
