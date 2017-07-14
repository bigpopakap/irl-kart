package irl.fw.engine.world;

import irl.fw.engine.entity.Entity;
import irl.util.serialization.JSONSerializable;

import java.util.Collection;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/6/15
 */
public interface World extends JSONSerializable {

    double getMinX();
    double getMaxX();
    double getMinY();
    double getMaxY();

    default double getWidth() {
        return getMaxX() - getMinX();
    }

    default double getHeight() {
        return getMaxY() - getMinY();
    }

    Collection<Entity> getEntities();

}
