package irl.kart.entities.items.actions.itemuser;

import irl.fw.engine.entity.Entity;
import irl.kart.entities.items.Item;

import java.util.Optional;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/14/15
 */
public class ItemUserAdaptor<T extends Entity & ItemUser> implements ItemUser {

    private final T user;
    private Optional<Item> item = Optional.empty();

    public ItemUserAdaptor(T user) {
        this.user = user;
    }

    @Override
    public void takeItem(Item item) {
        if (!this.item.isPresent()) {
            this.item = Optional.of(item);
        }
    }

    @Override
    public void holdItem() {
        if (this.item.isPresent()) {
            Item item = this.item.get();

            if (item.isHoldable()) {
                //TODO do something here
                System.out.println("HOLD ITEM");
            } else {
                //assume that non-holdable items should get
                //used when the kart tries to "hold" it
                useItem();
            }
        }
    }

    @Override
    public void useItem() {
        if (item.isPresent()) {
            item.get().doUseItem(user);
            clearItem();
        }
    }

    private void clearItem() {
        this.item = Optional.empty();
    }

}
