package irl.kart.entities.items.actions.holdable;

import irl.fw.engine.entity.Entity;
import irl.fw.engine.entity.factory.EntityConfig;
import irl.fw.engine.entity.factory.InitializedEntityFactory;
import irl.fw.engine.entity.state.EntityStateUpdate;
import irl.kart.entities.items.actions.itemuser.ItemUser;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/16/15
 */
public interface InitializedHoldableEntityFactory<T> extends InitializedEntityFactory<T> {

    @Override
    default T create(EntityConfig config, EntityStateUpdate suggestedState) {
        return create(config, suggestedState, null);
    }

    <U extends Entity & ItemUser> T create(EntityConfig config, EntityStateUpdate suggestedState, U user);

}
