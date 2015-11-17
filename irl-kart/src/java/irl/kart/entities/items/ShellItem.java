package irl.kart.entities.items;

import irl.fw.engine.entity.Entity;
import irl.fw.engine.entity.factory.EntityConfig;
import irl.fw.engine.entity.state.EntityState;
import irl.fw.engine.entity.state.EntityStateBuilder;
import irl.fw.engine.entity.state.EntityStateUpdate;
import irl.fw.engine.events.EngineEvent;
import irl.fw.engine.events.UpdateEntity;
import irl.fw.engine.geometry.Vector2D;
import irl.kart.entities.Kart;
import irl.kart.entities.items.actions.holdable.HoldableItemAdaptor;
import irl.kart.entities.items.actions.holdable.InitializedHoldableEntityFactory;
import irl.kart.entities.items.actions.itemuser.ItemUser;
import irl.kart.entities.weapons.Shell;
import irl.util.reactiveio.EventQueue;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/11/15
 */
public class ShellItem extends BaseItem {

    private static final double DISTANCE_WHEN_HELD = 2*Kart.KART_LENGTH/3 + Shell.SIZE/2;
    private static final double DISTANCE_WHEN_FIRED = DISTANCE_WHEN_HELD + Kart.KART_LENGTH/2;

    private final EventQueue<EngineEvent> eventQueue;
    private final HoldableItemAdaptor<Shell> holdable;

    public ShellItem(EventQueue<EngineEvent> eventQueue) {
        this.eventQueue = eventQueue;

        this.holdable = new HoldableItemAdaptor<>(
            eventQueue, onRemoved,
            new ShellItemFactory(eventQueue),
            DISTANCE_WHEN_HELD
        );
    }

    @Override
    public <T extends Entity & ItemUser> void doUseItem(T user) {
        holdable.doUseItem(user, shell -> {
            //re-center the shell and give it some velocity
            EntityState userState = user.getState();
            Vector2D userCenter = userState.getCenter();

            Vector2D shellVelocity = new Vector2D(0, Shell.SPEED).rotate(userState.getRotation());
            Vector2D shellCenter = userCenter.add(
                shellVelocity.scaleTo(DISTANCE_WHEN_FIRED)
            );

            eventQueue.mergeIn(new UpdateEntity(
                shell.getEngineId(),
                new EntityStateUpdate()
                    .center(shellCenter)
                    .friction(Shell.FRICTION)
                    .velocity(shellVelocity)
                    .angularVelocity(Shell.ROTATIONAL_SPEED)
            ));

            onUsed.run();
        });
    }

    @Override
    public <T extends Entity & ItemUser> void doHoldItem(T user) {
        holdable.doHoldItem(user);
    }

    private static class ShellItemFactory implements InitializedHoldableEntityFactory<Shell> {

        private final EventQueue<EngineEvent> eventQueue;

        private ShellItemFactory(EventQueue<EngineEvent> eventQueue) {
            this.eventQueue = eventQueue;
        }

        @Override
        public <U extends Entity & ItemUser> Shell create(EntityConfig config, EntityStateUpdate state, U user) {
            return new Shell(
                config,
                new EntityStateBuilder().defaults()
                        .shape(Shell.SHAPE)
                        .center(state.getCenter())
                        .friction(state.getFriction())
                        .build(),
                user.getEngineId(),
                eventQueue
            );
        }

    }

}
