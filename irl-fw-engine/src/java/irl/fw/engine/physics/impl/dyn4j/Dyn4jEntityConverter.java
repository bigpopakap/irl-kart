package irl.fw.engine.physics.impl.dyn4j;

import irl.fw.engine.entity.Entity;
import irl.fw.engine.entity.EntityId;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.World;

import java.util.Optional;

import static irl.fw.engine.physics.impl.dyn4j.Dyn4jConverter.fromId;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/15/15
 */
public class Dyn4jEntityConverter {

    private final World world;

    public Dyn4jEntityConverter(World world) {
        this.world = world;
    }

    public Optional<Body> fromEntity(Entity entity) {
        return fromEntity(entity.getEngineId());
    }

    public Optional<Body> fromEntity(EntityId entityId) {
        return findBody(entityId);
    }

    public Entity toEntity(Body body) {
        return (Entity) body.getUserData();
    }

    private Optional<Body> findBody(EntityId entityId) {
        return world.getBodies().stream()
                .filter(body -> body.getId().equals(fromId(entityId)))
                .findFirst();
    }

}
