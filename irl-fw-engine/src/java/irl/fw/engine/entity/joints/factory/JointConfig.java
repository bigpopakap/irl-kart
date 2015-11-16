package irl.fw.engine.entity.joints.factory;

import irl.fw.engine.entity.EntityId;
import irl.fw.engine.entity.joints.JointPoint;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/15/15
 */
public class JointConfig {

    private EntityId id;
    private JointPoint point1;
    private JointPoint point2;

    public JointConfig() {
        //do nothing... this is sort of a builder class
    }

    public EntityId getId() {
        return id;
    }

    public JointConfig setId(EntityId id) {
        if (getId() != null) {
            throw new UnsupportedOperationException("Can't re-set the ID");
        } else {
            this.id = id;
        }
        return this;
    }

    public JointPoint getPoint1() {
        return point1;
    }

    public JointConfig setPoint1(JointPoint point1) {
        this.point1 = point1;
        return this;
    }

    public JointPoint getPoint2() {
        return point2;
    }

    public JointConfig setPoint2(JointPoint point2) {
        this.point2 = point2;
        return this;
    }

}
