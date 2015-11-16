package irl.kart.entities.items;

import irl.fw.engine.entity.Entity;
import irl.fw.engine.entity.state.EntityStateBuilder;
import irl.fw.engine.events.EngineEvent;
import irl.fw.engine.geometry.Angle;
import irl.kart.entities.items.actions.holdable.HoldableItemAdaptor;
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

    private static final double DISTANCE_WHEN_HELD = Kart.KART_LENGTH/2 + Banana.SIZE;

    private final HoldableItemAdaptor<Banana> holdable;

    public BananaItem(EventQueue<EngineEvent> eventQueue) {
        holdable = new HoldableItemAdaptor<>(
            eventQueue, onRemoved,
            (config, state) -> new Banana(
                config,
                new EntityStateBuilder().defaults()
                        .shape(Banana.SHAPE)
                        .center(state.getCenter())
                        .rotation(Angle.random())
                        .friction(Banana.FRICTION)
                        .restitution(Banana.RESTITUTION)
                        .build(),
                eventQueue
            ),
            DISTANCE_WHEN_HELD
        );
    }

    @Override
    public <T extends Entity & ItemUser> void doUseItem(T user) {
        //for bananas, using the item just means unholding it
        holdable.doUseItem(user, banana -> {
            onUsed.run();
        });
    }

    @Override
    public <T extends Entity & ItemUser> void doHoldItem(T user) {
        holdable.doHoldItem(user);
    }

}
