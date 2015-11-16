package irl.fw.engine.entity.joints;

import irl.fw.engine.entity.Entity;
import irl.fw.engine.geometry.Vector2D;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/15/15
 */
public class JointPoint {

    private final Entity entity;
    private final Vector2D location;

    public JointPoint(Entity entity, Vector2D location) {
        this.entity = entity;
        this.location = location;
    }

    public Entity getEntity() {
        return entity;
    }

    public Vector2D getLocation() {
        return location;
    }

}
