package irl.kart.entities.items.actions.holdable;

import irl.fw.engine.entity.Entity;
import irl.fw.engine.entity.actions.remove.RemovableEntity;
import irl.fw.engine.entity.factory.EntityFactory;
import irl.fw.engine.entity.joints.Joint;
import irl.fw.engine.entity.joints.factory.DistanceJointFactory;
import irl.fw.engine.entity.state.EntityState;
import irl.fw.engine.entity.state.EntityStateUpdate;
import irl.fw.engine.events.AddEntity;
import irl.fw.engine.events.AddJoint;
import irl.fw.engine.events.EngineEvent;
import irl.fw.engine.geometry.Angle;
import irl.fw.engine.geometry.Vector2D;
import irl.kart.entities.items.actions.itemuser.ItemUser;
import irl.util.callbacks.Callback;
import irl.util.reactiveio.EventQueue;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/16/15
 */
public class HoldableItemAdaptor<T extends Entity & HoldableEntity & RemovableEntity> {

    private static final double FRICTION_WHEN_HELD = 8.0;

    private final EventQueue<EngineEvent> eventQueue;
    private final Callback onRemoved;
    private final InitializedHoldableEntityFactory<T> factory;
    private final double distanceBetweenCenters;

    private volatile boolean isHeld = false;
    private volatile Joint joint = null;
    private volatile T createdEntity = null;
    private final List<String> entityCallbacks = new ArrayList<>();

    public HoldableItemAdaptor(EventQueue<EngineEvent> eventQueue,
                               Callback onRemoved,
                               InitializedHoldableEntityFactory<T> factory,
                               double distanceBetweenCenters) {
        this.eventQueue = eventQueue;
        this.onRemoved = onRemoved;
        this.factory = factory;
        this.distanceBetweenCenters = distanceBetweenCenters;
    }

    public <U extends Entity & ItemUser> void doUseItem(U user, Consumer<T> afterUnhold) {
        doHoldItem(user, () -> {
            joint.remove();
            unlinkEntity();
            afterUnhold.accept(createdEntity);
        });
    }

    public <U extends Entity & ItemUser> void doHoldItem(U user) {
        doHoldItem(user, () -> {});
    }

    private <U extends Entity & ItemUser> void doHoldItem(U user, Callback afterHold) {
        if (isHeld) {
            //only hold the item once
            afterHold.run();
            return;
        }
        isHeld = true;

        EntityFactory<T> wrappedFactory = wrapFactory(user, factory, joint -> {
            this.joint = joint;
        });

        AddEntity addItem = new AddEntity(entityConfig -> {
            this.createdEntity = wrappedFactory.create(entityConfig);
            return this.createdEntity;
        });
        eventQueue.mergeIn(addItem);
    }

    private synchronized void onEntityRemoved() {
        //don't need to delete the joint because it will be
        //deleted when one of its anchors is removed
        isHeld = false;
        unlinkEntity();
        onRemoved.run();
    }

    private synchronized void onJointRemoved() {
        isHeld = false;
    }

    private void unlinkEntity() {
        synchronized (entityCallbacks) {
            for (String callbackId : entityCallbacks) {
                createdEntity.removeRemoveHandler(callbackId);
            }
            entityCallbacks.clear();
        }
    }

    private <U extends Entity & ItemUser> EntityFactory<T> wrapFactory(U user, InitializedHoldableEntityFactory<T> innerFactory,
                                                                       Consumer<Joint> afterCreate) {
        return entityConfig -> {
            T createdEntity = innerFactory.create(
                entityConfig,
                new EntityStateUpdate()
                    .center(calculateNewEntityCenter(user))
                    .friction(FRICTION_WHEN_HELD),
                user
            );
            entityCallbacks.add(
                createdEntity.addRemoveHandler(this::onEntityRemoved)
            );

            eventQueue.mergeIn(addJoint(user, createdEntity, afterCreate));

            return createdEntity;
        };
    }

    private <U extends Entity & ItemUser> AddJoint addJoint(U user, T createdEntity, Consumer<Joint> afterCreate) {
        return new AddJoint(new DistanceJointFactory(
            createdEntity.getHoldPoint(),
            user.getItemHoldPoint(),
            eventQueue,
            joint -> {
                joint.addRemoveHandler(this::onJointRemoved);
                afterCreate.accept(joint);
            }
        ));
    }

    private <U extends Entity & ItemUser> Vector2D calculateNewEntityCenter(U user) {
        EntityState userState = user.getState();
        Vector2D userCenter = userState.getCenter();

        Vector2D itemDirection = new Vector2D(0, 1)
                .rotate(userState.getRotation())
                .rotate(Angle.HALF);
        Vector2D itemCenter = userCenter.add(
                itemDirection.scaleTo(distanceBetweenCenters)
        );

        return itemCenter;
    }

}
