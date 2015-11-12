package irl.kart.entities.items;

import irl.fw.engine.entity.EntityId;
import irl.fw.engine.entity.VirtualEntity;
import irl.fw.engine.entity.state.EntityState;
import irl.fw.engine.events.EngineEvent;
import irl.util.reactiveio.Pipe;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/11/15
 */
public class ItemBoxPedestal extends VirtualEntity {

    private final Pipe<EngineEvent> eventQueue;

    public ItemBoxPedestal(EntityId engineId, EntityState initState,
                           Pipe<EngineEvent> eventQueue) {
        super(engineId, initState);
        this.eventQueue = eventQueue;
    }

    //TODO recreate an ItemBox every so often
    //TODO this object should be invisible, and no collisions

}
