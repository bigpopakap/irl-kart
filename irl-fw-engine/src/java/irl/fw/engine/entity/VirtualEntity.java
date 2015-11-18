package irl.fw.engine.entity;

import irl.fw.engine.entity.factory.EntityConfig;
import irl.fw.engine.entity.state.EntityState;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 10/29/15
 */
public abstract class VirtualEntity extends Entity {

    public VirtualEntity(EntityConfig entityConfig, EntityState initState) {
        super(
            entityConfig
                //don't overwrite, but default to NORMAL
                .type_safe(EntityType.NORMAL),
            initState
        );
    }

}
