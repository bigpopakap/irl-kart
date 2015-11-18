package irl.fw.engine.physics.impl.dyn4j;

import irl.fw.engine.entity.EntityId;
import irl.fw.engine.entity.EntityType;
import irl.fw.engine.geometry.Angle;
import irl.fw.engine.geometry.Vector2D;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;

import java.util.UUID;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/6/15
 */
class Dyn4jConverter {

    public static UUID fromId(EntityId entitId) {
        return UUID.fromString(entitId.toString());
    }

    public static MassType fromEntityType(EntityType type) {
        switch (type) {
            case NORMAL: return MassType.NORMAL;
            case FIXED: return MassType.INFINITE;
            case FIXED_SPIN: return MassType.FIXED_ANGULAR_VELOCITY;
            default:
                throw new IllegalStateException("Unexpected entity type: " + type);
        }
    }

    public static EntityId toId(UUID id) {
        return new EntityId(id.toString());
    }

    public static Vector2 fromVector(Vector2D vector) {
        return new Vector2(vector.getX(), vector.getY());
    }

    public static Vector2D toVector(Vector2 vector) {
        return new Vector2D(vector.x, vector.y);
    }

    public static Angle toRadAngle(double rads) {
        return Angle.rad(rads);
    }

}
