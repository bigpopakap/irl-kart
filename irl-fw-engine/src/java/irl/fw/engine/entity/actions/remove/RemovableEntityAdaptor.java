package irl.fw.engine.entity.actions.remove;

import irl.fw.engine.entity.EngineElement;
import irl.fw.engine.events.EngineEvent;
import irl.fw.engine.events.RemoveEntity;
import irl.util.callbacks.Callback;
import irl.util.callbacks.Callbacks;
import irl.util.reactiveio.EventQueue;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/12/15
 */
public class RemovableEntityAdaptor implements RemovableEntity {

    private final EngineElement entity;
    private final EventQueue<EngineEvent> eventQueue;
    private final Callbacks onRemove;
    private boolean hasQueuedRemove = false;

    public RemovableEntityAdaptor(EngineElement entity, EventQueue<EngineEvent> eventQueue) {
        this.entity = entity;
        this.eventQueue = eventQueue;
        this.onRemove = new Callbacks();
    }

    @Override
    public void remove() {
        if (!hasQueuedRemove) {
            eventQueue.mergeIn(new RemoveEntity(entity.getEngineId()));
            onRemove.run();
            hasQueuedRemove = true;
        }
    }

    @Override
    public String onRemove(Callback callback) {
        return onRemove.add(callback);
    }

}
