package irl.fw.engine.entity.factory;

import irl.fw.engine.entity.EntityId;
import irl.util.universe.UniverseElementFactory;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/11/15
 */
public interface EntityFactory<T> extends UniverseElementFactory<T> {

    @Override
    default T create(String id) {
        return create(new EntityConfig().setId(new EntityId(id)));
    }

    T create(EntityConfig config);

}
