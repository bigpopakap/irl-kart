package irl.fw.engine.world;

import irl.fw.engine.entity.Entity;
import irl.util.serialization.JSONSerializable;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

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

    @Override
    default String toJSON() {
        // TODO use Gson
        String entitiesString = Arrays.toString(
            getEntities().stream()
                .map(Entity::toJSON)
                .collect(Collectors.toList())
                .toArray()
        );

        return String.format(
            "{ " +
                "\"dimensions\": { " +
                    "\"minX\": %s, " +
                    "\"maxX\": %s, " +
                    "\"minY\": %s, " +
                    "\"maxY\": %s, " +
                    "\"width\": %s, " +
                    "\"height\": %s " +
                "}, " +
                "\"entities\": %s" +
            "}",
            getMinX(), getMaxX(),
            getMinY(), getMaxY(),
            getWidth(), getHeight(),
            entitiesString
        );
    }
}
