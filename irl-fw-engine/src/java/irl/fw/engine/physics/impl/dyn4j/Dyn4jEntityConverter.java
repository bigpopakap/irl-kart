package irl.fw.engine.physics.impl.dyn4j;

import irl.fw.engine.entity.Entity;
import irl.fw.engine.entity.EntityId;
import irl.util.CompareUtils;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.World;
import org.dyn4j.dynamics.joint.Joint;

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

    public Optional<Joint> fromJoint(irl.fw.engine.entity.joints.Joint joint) {
        return fromJoint(joint.getEngineId());
    }

    public Optional<Joint> fromJoint(EntityId jointId) {
        return findJoint(jointId);
    }

    public Entity toEntity(Body body) {
        return (Entity) body.getUserData();
    }

    private Optional<Body> findBody(EntityId entityId) {
        return world.getBodies().parallelStream()
                .filter(body -> CompareUtils.equal(body.getId(), fromId(entityId)))
                .findFirst();
    }

    private Optional<Joint> findJoint(EntityId entityId) {
        return world.getJoints().parallelStream()
                .filter(joint -> CompareUtils.equal(joint.getId(), fromId(entityId)))
                .findFirst();
    }

}
