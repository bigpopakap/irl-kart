package irl.fw.engine.events;

import irl.fw.engine.entity.EntityInstance;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 10/30/15
 */
public class EntityCollision implements EngineEvent {

    private final EntityInstance entity1;
    private final EntityInstance entity2;

    public EntityCollision(EntityInstance entity1, EntityInstance entity2) {
        this.entity1 = entity1;
        this.entity2 = entity2;
    }

    public EntityInstance getEntity1() {
        return entity1;
    }

    public EntityInstance getEntity2() {
        return entity2;
    }

}
