package irl.kart.entities.items;

import irl.fw.engine.entity.Entity;
import irl.kart.entities.items.actions.itemuser.ItemUser;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/11/15
 */
public interface Item {

    boolean isHoldable();
    <T extends Entity & ItemUser> void doUseItem(T user);

}
