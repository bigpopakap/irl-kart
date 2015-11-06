package irl.fw.engine.world;

import irl.fw.engine.entity.EntityInstance;

import java.util.Collection;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/6/15
 */
public interface World {

    double getMinX();
    double getMaxX();
    double getMinY();
    double getMaxY();

    Collection<EntityInstance> getEntities();

}
