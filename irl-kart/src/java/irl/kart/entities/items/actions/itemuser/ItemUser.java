package irl.kart.entities.items.actions.itemuser;

import irl.kart.entities.items.Item;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/14/15
 */
public interface ItemUser {

    void takeItem(Item item);
    void holdItem();
    void useItem();

}
