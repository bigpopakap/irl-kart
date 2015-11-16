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
public class ItemUserAdaptor<T extends Entity & ItemUser> {

    private final T user;
    private Optional<Item> item = Optional.empty();

    public ItemUserAdaptor(T user) {
        this.user = user;
    }

    public void takeItem(Item item) {
        if (!this.item.isPresent()) {
            this.item = Optional.of(item);
            item.onRemoved(this::clearItem);
            item.onUsed(this::clearItem);
        }
    }

    public void holdItem() {
        if (this.item.isPresent()) {
            Item item = this.item.get();
            item.doHoldItem(user);
        }
    }

    public void useItem() {
        if (item.isPresent()) {
            item.get().doUseItem(user);
        }
    }

    private void clearItem() {
        this.item = Optional.empty();
    }

}
