package irl.fw.engine.entity.joints.factory;

import irl.fw.engine.entity.joints.Joint;
import irl.fw.engine.entity.joints.JointPoint;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/15/15
 */
public interface JointFactory<T extends Joint> {

    Class<T> getType();
    JointPoint getPoint1();
    JointPoint getPoint2();

    T create(JointConfig jointConfig);

}
