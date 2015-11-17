package irl.kart.entities.items;

import irl.fw.engine.entity.Entity;
import irl.fw.engine.entity.factory.EntityConfig;
import irl.fw.engine.entity.state.EntityStateBuilder;
import irl.fw.engine.entity.state.EntityStateUpdate;
import irl.fw.engine.events.EngineEvent;
import irl.fw.engine.events.UpdateEntity;
import irl.fw.engine.geometry.Angle;
import irl.kart.entities.items.actions.holdable.HoldableItemAdaptor;
import irl.kart.entities.items.actions.holdable.InitializedHoldableEntityFactory;
import irl.kart.entities.weapons.Banana;
import irl.kart.entities.Kart;
import irl.kart.entities.items.actions.itemuser.ItemUser;
import irl.util.reactiveio.EventQueue;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/15/15
 */
public class BananaItem extends BaseItem {

    private static final double DISTANCE_WHEN_HELD = Kart.KART_LENGTH/2 + Banana.SIZE/2;

    private final EventQueue<EngineEvent> eventQueue;
    private final HoldableItemAdaptor<Banana> holdable;

    public BananaItem(EventQueue<EngineEvent> eventQueue) {
        this.eventQueue = eventQueue;

        holdable = new HoldableItemAdaptor<>(
            eventQueue, onRemoved,
            new BananaItemFactory(eventQueue),
            DISTANCE_WHEN_HELD
        );
    }

    @Override
    public <T extends Entity & ItemUser> void doUseItem(T user) {
        holdable.doUseItem(user, banana -> {
            eventQueue.mergeIn(new UpdateEntity(
                banana.getEngineId(),
                new EntityStateUpdate()
                    .friction(Banana.FRICTION)
            ));

            onUsed.run();
        });
    }

    @Override
    public <T extends Entity & ItemUser> void doHoldItem(T user) {
        holdable.doHoldItem(user);
    }

    /**
     * TODO bigpopakap Javadoc this class
     *
     * @author bigpopakap
     * @since 11/16/15
     */
    private static class BananaItemFactory implements InitializedHoldableEntityFactory<Banana> {

        private final EventQueue<EngineEvent> eventQueue;

        private BananaItemFactory(EventQueue<EngineEvent> eventQueue) {
            this.eventQueue = eventQueue;
        }

        @Override
        public <U extends Entity & ItemUser> Banana create(EntityConfig config, EntityStateUpdate state, U user) {
            return new Banana(
                config,
                new EntityStateBuilder().defaults()
                        .shape(Banana.SHAPE)
                        .center(state.getCenter())
                        .rotation(Angle.random())
                        .friction(state.getFriction())
                        .restitution(Banana.RESTITUTION)
                        .build(),
                eventQueue
            );
        }

    }

}
