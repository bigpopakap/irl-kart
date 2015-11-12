package irl.fw.engine.events;

import irl.fw.engine.entity.Entity;
import irl.fw.engine.entity.EntityFactory;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 10/30/15
 */
public class AddEntity implements EngineEvent {

    private final EntityFactory<? extends Entity> entityFactory;

    public AddEntity(EntityFactory<? extends Entity> entityFactory) {
        this.entityFactory = entityFactory;
    }

    public EntityFactory<? extends Entity> getEntityFactory() {
        return entityFactory;
    }

}
