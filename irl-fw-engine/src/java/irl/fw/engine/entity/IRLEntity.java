package irl.fw.engine.entity;

import irl.fw.engine.entity.state.EntityState;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 10/29/15
 */
public abstract class IRLEntity extends Entity {

    public IRLEntity(EntityId engineId, EntityState initState) {
        super(engineId, initState);
    }

    @Override
    public final boolean isVirtual() {
        return false;
    }
}
