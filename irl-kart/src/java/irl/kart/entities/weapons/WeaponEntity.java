package irl.kart.entities.weapons;

import irl.fw.engine.entity.Entity;
import irl.fw.engine.entity.VirtualEntity;
import irl.fw.engine.entity.actions.remove.RemovableEntity;
import irl.fw.engine.entity.actions.remove.RemovableEntityAdaptor;
import irl.fw.engine.entity.factory.EntityConfig;
import irl.fw.engine.entity.state.EntityState;
import irl.fw.engine.events.EngineEvent;
import irl.util.callbacks.Callback;
import irl.util.reactiveio.EventQueue;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/15/15
 */
public class WeaponEntity extends VirtualEntity implements RemovableEntity {

    private final RemovableEntityAdaptor remover;

    public WeaponEntity(EntityConfig entityConfig, EntityState initState,
                        EventQueue<EngineEvent> eventQueue) {
        super(entityConfig, initState);
        remover = new RemovableEntityAdaptor(this, eventQueue);
    }

    @Override
    public boolean collide(Entity other) {
        if (other instanceof WeaponTarget) {
            WeaponTarget target = (WeaponTarget) other;
            target.hitBy(this);
            remove();
            return false;
        } else if (other instanceof WeaponEntity) {
            remove();
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void remove() {
        remover.remove();
    }

    @Override
    public String onRemove(Callback callback) {
        return remover.onRemove(callback);
    }

}
