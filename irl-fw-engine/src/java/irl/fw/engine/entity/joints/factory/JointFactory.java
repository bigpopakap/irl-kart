package irl.fw.engine.entity.joints.factory;

import irl.fw.engine.entity.factory.EntityConfig;
import irl.fw.engine.entity.joints.Joint;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/15/15
 */
public interface JointFactory<T extends Joint> {

    T create(EntityConfig entityConfig);

}
