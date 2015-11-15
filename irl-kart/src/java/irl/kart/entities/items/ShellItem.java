package irl.kart.entities.items;

import irl.fw.engine.entity.Entity;
import irl.fw.engine.entity.factory.EntityConfig;
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
import irl.kart.entities.items.actions.holdableitem.HoldableItem;
import irl.kart.entities.items.actions.holdableitem.HoldableItemAdaptor;
import irl.kart.entities.weapons.Banana;
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
    private final HoldableItemAdaptor<Shell> holdable;

    public ShellItem(EventQueue<EngineEvent> eventQueue) {
        this.eventQueue = eventQueue;
        this.holdable = new HoldableItemAdaptor<>();
    }

    @Override
    public synchronized <T extends Entity & ItemUser> void doUseItem(T user) {
        EntityState userState = user.getState();
        Vector2D kartCenter = userState.getCenter();

        Vector2D shellVelocity = new Vector2D(0, Shell.SPEED).rotate(userState.getRotation());
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
    public synchronized <T extends Entity & ItemUser> void doHoldItem(T user) {
        EntityState userState = user.getState();
        Vector2D userCenter = userState.getCenter();

        Vector2D shellDirection = new Vector2D(0, 1)
                .rotate(userState.getRotation())
                .rotate(Angle.HALF);
        Vector2D shellCenter = userCenter.add(
                shellDirection.scaleTo(Kart.KART_LENGTH + 0.25*Shell.SIZE)
        );

        if (holdable.isHeld()) {
            Shell heldShell = holdable.getHeldEntity();
            UpdateEntity shellUpdate = new UpdateEntity(
                heldShell.getEngineId(),
                new EntityStateUpdate().center(shellCenter)
            );
            eventQueue.mergeIn(shellUpdate);
            System.out.println("Shell at center: " + shellCenter);
        } else {
            //create the new held entity
            EntityFactory<Shell> factory = entityConfig -> {
                Shell newShell = new Shell(
                        entityConfig,
                        new EntityStateBuilder().defaults()
                                .shape(Shell.SHAPE)
                                .center(shellCenter)
                                .build(),
                        user.getEngineId(),
                        eventQueue
                );
                holdable.setCreatedEntity(newShell);
                return newShell;
            };

            AddEntity addShell = new AddEntity(factory);
            eventQueue.mergeIn(addShell);
        }
    }

    @Override
    public synchronized void remove() {
        holdable.remove();
    }

    @Override
    public synchronized String onRemove(Callback callback) {
        return holdable.onRemove(callback);
    }

}
