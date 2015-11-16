package irl.kart.entities.items;

import irl.fw.engine.entity.Entity;
import irl.fw.engine.entity.factory.EntityFactory;
import irl.fw.engine.entity.state.EntityState;
import irl.fw.engine.entity.state.EntityStateBuilder;
import irl.fw.engine.entity.state.EntityStateUpdate;
import irl.fw.engine.events.AddEntity;
import irl.fw.engine.events.EngineEvent;
import irl.fw.engine.events.UpdateEntity;
import irl.fw.engine.geometry.Angle;
import irl.fw.engine.geometry.Vector2D;
import irl.kart.entities.Kart;
import irl.kart.entities.items.actions.holdableitem.HoldableItemAdaptor;
import irl.kart.entities.weapons.Shell;
import irl.kart.entities.items.actions.itemuser.ItemUser;
import irl.util.reactiveio.EventQueue;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/11/15
 */
public class ShellItem extends BaseItem {

    private final EventQueue<EngineEvent> eventQueue;
    private final HoldableItemAdaptor<Shell> holdable;

    public ShellItem(EventQueue<EngineEvent> eventQueue) {
        this.eventQueue = eventQueue;
        this.holdable = new HoldableItemAdaptor<>(this.eventQueue, entityConfig -> new Shell(
            entityConfig,
            new EntityStateBuilder().defaults()
                .shape(Shell.SHAPE)
                .build(),
            null, //TODO WTF HAPPENS HERE?
            eventQueue
        ));
    }

    @Override
    public synchronized <T extends Entity & ItemUser> void doUseItem(T user) {
        holdable.doUseItem(user);
    }

    @Override
    public <T extends Entity & ItemUser> void doHoldItem(T user) {
        holdable.doHoldItem(user, Kart.KART_LENGTH + Shell.SIZE);
    }

}
