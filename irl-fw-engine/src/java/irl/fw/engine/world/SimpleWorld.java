package irl.fw.engine.world;

import irl.fw.engine.entity.Entity;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/6/15
 */
public class SimpleWorld implements World {

    private final Collection<Entity> entities;
    private final double minX, maxX, minY, maxY;

    public SimpleWorld(Collection<Entity> entities,
                       double minX, double maxX,
                       double minY, double maxY) {
        this.entities = Collections.unmodifiableCollection(entities);
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
    }

    @Override
    public Collection<Entity> getEntities() {
        return entities;
    }

    @Override
    public double getMinX() {
        return minX;
    }

    @Override
    public double getMaxX() {
        return maxX;
    }

    @Override
    public double getMinY() {
        return minY;
    }

    @Override
    public double getMaxY() {
        return maxY;
    }

    @Override
    public String toJSON() {
        // TODO use Gson
        String entitiesString = Arrays.toString(
            getEntities().stream()
                .map(Entity::toJSON)
                .collect(Collectors.toList())
                .toArray()
        );

        return String.format(
            "{ " +
                "dimensions: { " +
                    "minX: %s, " +
                    "maxX: %s, " +
                    "minY: %s, " +
                    "maxY: %s, " +
                    "width: %s, " +
                    "height: %s " +
                "} " +
                "entities: %s" +
            "}",
            getMinX(), getMaxX(),
            getMinY(), getMaxY(),
            getWidth(), getHeight(),
            entitiesString
        );
    }
}
