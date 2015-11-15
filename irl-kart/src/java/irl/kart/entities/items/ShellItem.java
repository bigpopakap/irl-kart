package irl.kart.entities.items;

import irl.fw.engine.entity.Entity;
import irl.fw.engine.entity.state.EntityState;
import irl.fw.engine.entity.state.EntityStateBuilder;
import irl.fw.engine.events.AddEntity;
import irl.fw.engine.events.EngineEvent;
import irl.fw.engine.geometry.Vector2D;
import irl.kart.entities.Kart;
import irl.kart.entities.items.actions.holdableitem.HoldableItem;
import irl.kart.entities.items.actions.holdableitem.HoldableItemAdaptor;
import irl.kart.entities.weapons.Shell;
import irl.kart.entities.items.actions.itemuser.ItemUser;
import irl.util.callbacks.Callback;
import irl.util.reactiveio.EventQueue;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/11/15
 */
public class ShellItem implements HoldableItem {

    private final EventQueue<EngineEvent> eventQueue;
    private final HoldableItemAdaptor holdable;

    public ShellItem(EventQueue<EngineEvent> eventQueue) {
        this.eventQueue = eventQueue;
        this.holdable = new HoldableItemAdaptor();
    }

    @Override
    public <T extends Entity & ItemUser> void doUseItem(T user) {
        EntityState kartState = user.getState();
        Vector2D kartCenter = kartState.getCenter();

        Vector2D shellVelocity = new Vector2D(0, Shell.SPEED).rotate(kartState.getRotation());
        Vector2D shellCenter = kartCenter.add(
                shellVelocity.scaleTo(Kart.KART_LENGTH + 0.25*Shell.SIZE)
        );

        AddEntity addShell = new AddEntity(entityConfig -> new Shell(
                entityConfig,
                new EntityStateBuilder().defaults()
                        .shape(Shell.SHAPE)
                        .center(shellCenter)
                        .velocity(shellVelocity)
                        .angularVelocity(Shell.ROTATIONAL_SPEED)
                        .build(),
                user.getEngineId(),
                eventQueue
        ));

        eventQueue.mergeIn(addShell);
    }

    @Override
    public <T extends Entity & ItemUser> void doHoldItem(T user) {
        //TODO
        System.out.println("Shell being held");
//        holdable.createdEntity(null);
    }

    @Override
    public void remove() {
        holdable.remove();
    }

    @Override
    public String onRemove(Callback callback) {
        return holdable.onRemove(callback);
    }

}
