package irl.kart.entities.items;

import irl.fw.engine.entity.Entity;
import irl.kart.entities.items.actions.itemuser.ItemUser;
import irl.util.callbacks.Callback;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/11/15
 */
public interface Item {

    <T extends Entity & ItemUser> void doUseItem(T user);

    default <T extends Entity & ItemUser> void doHoldItem(T user) {
        //by default, items just get used when you try to hold them
        doUseItem(user);
    }

    void onUsed(Callback onUsed);
    void onRemoved(Callback onRemoved);

}
