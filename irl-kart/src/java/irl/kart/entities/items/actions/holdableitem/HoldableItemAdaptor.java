package irl.kart.entities.items.actions.holdableitem;

import irl.fw.engine.entity.Entity;
import irl.fw.engine.entity.actions.remove.RemovableEntity;
import irl.util.callbacks.Callback;
import irl.util.callbacks.Callbacks;

import java.util.Optional;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/15/15
 */
public class HoldableItemAdaptor<T extends Entity> {

    private Optional<RemovableEntity> entity = Optional.empty();
    private final Callbacks onRemove;

    public HoldableItemAdaptor() {
        this.onRemove = new Callbacks();
    }

    private void setCreatedEntity(RemovableEntity entity) {
        entity.onRemove(onRemove);
        this.entity = Optional.ofNullable(entity);
    }

    public void remove() {
        if (entity.isPresent()) {
            entity.get().remove();
        }
    }

    public String onRemove(Callback callback) {
        return onRemove.add(callback);
    }

}
