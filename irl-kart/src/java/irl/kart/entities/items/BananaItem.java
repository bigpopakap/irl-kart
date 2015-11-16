package irl.kart.entities.items;

import irl.fw.engine.entity.Entity;
import irl.fw.engine.entity.joints.Joint;
import irl.fw.engine.entity.joints.JointPoint;
import irl.fw.engine.entity.joints.factory.DistanceJointFactory;
import irl.fw.engine.entity.state.EntityState;
import irl.fw.engine.entity.state.EntityStateBuilder;
import irl.fw.engine.events.AddEntity;
import irl.fw.engine.events.AddJoint;
import irl.fw.engine.events.EngineEvent;
import irl.fw.engine.geometry.Angle;
import irl.fw.engine.geometry.Vector2D;
import irl.kart.entities.weapons.Banana;
import irl.kart.entities.Kart;
import irl.kart.entities.items.actions.itemuser.ItemUser;
import irl.util.callbacks.Callback;
import irl.util.reactiveio.EventQueue;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/15/15
 */
public class BananaItem extends BaseItem {

    private final EventQueue<EngineEvent> eventQueue;
    private volatile boolean isHeld;
    private volatile Joint joint;

    public BananaItem(EventQueue<EngineEvent> eventQueue) {
        this.eventQueue = eventQueue;
    }

    @Override
    public <T extends Entity & ItemUser> void doUseItem(T user) {
        doHoldItem(user, () -> {
            joint.remove();
            onUsed.run();
        });
    }

    @Override
    public <T extends Entity & ItemUser> void doHoldItem(T user) {
        doHoldItem(user, () -> {});
    }

    private <T extends Entity & ItemUser> void doHoldItem(T user, Callback afterHold) {
        if (isHeld) {
            //only hold the item once
            afterHold.run();
            return;
        }
        isHeld = true;

        EntityState userState = user.getState();
        Vector2D userCenter = userState.getCenter();

        Vector2D bananaDirection = new Vector2D(0, 1)
                .rotate(userState.getRotation())
                .rotate(Angle.HALF);
        Vector2D bananaCenter = userCenter.add(
                bananaDirection.scaleTo(Kart.KART_LENGTH/2 + Banana.SIZE)
        );

        AddEntity addBanana = new AddEntity(entityConfig -> {
            Banana newBanana = new Banana(
                    entityConfig,
                    new EntityStateBuilder().defaults()
                            .shape(Banana.SHAPE)
                            .center(bananaCenter)
                            .rotation(Angle.random())
                            .build(),
                    eventQueue
            );

            newBanana.onRemove(() -> {
                isHeld = false;
                onRemoved.run();
            });

            eventQueue.mergeIn(new AddJoint(new DistanceJointFactory(
                new JointPoint(newBanana, newBanana.getState().getCenter()),
                user.getItemHoldPoint(),
                eventQueue,
                joint -> {
                    joint.onRemove(() -> isHeld = false);
                    this.joint = joint;
                    afterHold.run();
                }
            )));

            return newBanana;
        });

        eventQueue.mergeIn(addBanana);
    }

}
