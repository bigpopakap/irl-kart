package irl.kart.entities.items.actions.holdableitem;

import irl.fw.engine.entity.Entity;
import irl.fw.engine.entity.actions.remove.RemovableEntity;
import irl.kart.entities.items.Item;
import irl.kart.entities.items.actions.itemuser.ItemUser;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/15/15
 */
public interface HoldableItem extends Item, RemovableEntity {

    //TODO can we provide a default implementation here?
    <T extends Entity & ItemUser> void doHoldItem(T user);

}
