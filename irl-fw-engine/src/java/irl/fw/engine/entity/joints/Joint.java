package irl.fw.engine.entity.joints;

import irl.fw.engine.entity.EngineElement;
import irl.fw.engine.entity.EntityId;
import irl.fw.engine.entity.actions.remove.RemovableEntity;
import irl.fw.engine.entity.actions.remove.RemovableEntityAdaptor;
import irl.fw.engine.entity.joints.factory.JointConfig;
import irl.fw.engine.events.EngineEvent;
import irl.util.callbacks.Callback;
import irl.util.reactiveio.EventQueue;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/15/15
 */
//TODO this class should move into the Entity class hierarchy
public abstract class Joint implements EngineElement, RemovableEntity {

    private final EntityId engineId;
    private final JointPoint point1;
    private final JointPoint point2;

    private final RemovableEntityAdaptor entityRemover;

    public Joint(JointConfig jointConfig, EventQueue<EngineEvent> eventQueue) {
        if (jointConfig == null || jointConfig.getId() == null ||
            jointConfig.getPoint1() == null || jointConfig.getPoint2() == null) {
            throw new IllegalArgumentException("These cannot be null");
        }

        this.engineId = jointConfig.getId();
        this.point1 = jointConfig.getPoint1();
        this.point2 = jointConfig.getPoint2();

        entityRemover = new RemovableEntityAdaptor(this, eventQueue);
    }

    @Override
    public EntityId getEngineId() {
        return engineId;
    }

    public JointPoint getPoint1() {
        return point1;
    }

    public JointPoint getPoint2() {
        return point2;
    }

    @Override
    public void remove() {
        entityRemover.remove();
    }

    @Override
    public String addRemoveHandler(Callback callback) {
        return entityRemover.addRemoveHandler(callback);
    }

    @Override
    public void removeRemoveHandler(String callbackId) {
        entityRemover.removeRemoveHandler(callbackId);
    }

}
