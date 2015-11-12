package irl.fw.engine.entity;

import irl.fw.engine.entity.state.EntityState;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 10/29/15
 */
public abstract class VirtualEntity extends Entity {

    public VirtualEntity(EntityId engineId, EntityState initState) {
        super(engineId, initState);
    }

    @Override
    public final boolean isVirtual() {
        return true;
    }

}
