package irl.kart.entities.items;

import irl.fw.engine.entity.EntityId;
import irl.fw.engine.entity.VirtualEntity;
import irl.fw.engine.entity.state.EntityState;
import irl.fw.engine.entity.state.EntityStateBuilder;
import irl.fw.engine.events.AddEntity;
import irl.fw.engine.events.EngineEvent;
import irl.util.reactiveio.Pipe;
import rx.Observable;

import java.util.concurrent.TimeUnit;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/11/15
 */
public class ItemBoxPedestal extends VirtualEntity {

    private static final long ITEM_BOX_REGEN_DELAY = 2000;

    private final Pipe<EngineEvent> eventQueue;

    public ItemBoxPedestal(EntityId engineId, EntityState initState,
                           Pipe<EngineEvent> eventQueue) {
        super(engineId, initState);
        this.eventQueue = eventQueue;

        this.eventQueue.mergeIn(createItemBox());
    }

    private AddEntity createItemBox() {
        return new AddEntity(engineId -> new ItemBox(
            engineId,
            new EntityStateBuilder().defaults()
                    .center(getState().getCenter())
                    .shape(ItemBox.SHAPE)
                    .rotation(ItemBox.INIT_ROT)
                    .angularVelocity(ItemBox.ROTATION_SPEED)
                    .build(),
            eventQueue,
            this::queueItemBox
        ));
    }

    private void queueItemBox() {
        eventQueue.mergeIn(
            Observable.from(new AddEntity[] { createItemBox() })
                    .delay(ITEM_BOX_REGEN_DELAY, TimeUnit.MILLISECONDS)
        );
    }

    //TODO recreate an ItemBox every so often
    //TODO this object should be invisible, and no collisions

}
