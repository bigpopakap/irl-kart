package irl.fw.engine.entity.factory;

import irl.fw.engine.entity.state.EntityStateUpdate;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/16/15
 */
public interface InitializedEntityFactory<T> extends EntityFactory<T> {

    @Override
    default T create(EntityConfig config) {
        return create(config, new EntityStateUpdate());
    }

    T create(EntityConfig config, EntityStateUpdate suggestedState);

}
