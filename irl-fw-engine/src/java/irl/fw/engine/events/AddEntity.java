package irl.fw.engine.events;

import irl.fw.engine.entity.Entity;
import irl.fw.engine.physics.EntityState;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 10/30/15
 */
public class AddEntity implements EngineEvent {

    private final Entity entity;
    private final EntityState initialState;

    public AddEntity(Entity entity, EntityState initialState) {
        this.entity = entity;
        this.initialState = initialState;
    }

    public Entity getEntity() {
        return entity;
    }

    public EntityState getInitialState() {
        return initialState;
    }

}
