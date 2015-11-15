package irl.kart.entities.items;

import irl.fw.engine.entity.Entity;
import irl.fw.engine.entity.state.EntityState;
import irl.fw.engine.entity.state.EntityStateBuilder;
import irl.fw.engine.events.AddEntity;
import irl.fw.engine.events.EngineEvent;
import irl.fw.engine.geometry.Angle;
import irl.fw.engine.geometry.Vector2D;
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
public class BananaItem implements Item {

    private final EventQueue<EngineEvent> eventQueue;

    public BananaItem(EventQueue<EngineEvent> eventQueue) {
        this.eventQueue = eventQueue;
    }

    @Override
    public boolean isHoldable() {
        return true;
    }

    @Override
    public <T extends Entity & ItemUser> void doUseItem(T user) {
        EntityState kartState = user.getState();
        Vector2D kartCenter = kartState.getCenter();

        Vector2D bananaDirection = new Vector2D(0, 1)
                                        .rotate(kartState.getRotation())
                                        .rotate(Angle.HALF);
        Vector2D bananaCenter = kartCenter.add(
                bananaDirection.scaleTo(Kart.KART_LENGTH/2 + Banana.SIZE)
        );

        AddEntity addBanana = new AddEntity(entityConfig -> new Banana(
                entityConfig,
                new EntityStateBuilder().defaults()
                        .shape(Banana.SHAPE)
                        .center(bananaCenter)
                        .rotation(Angle.random())
                        .build(),
                eventQueue
        ));

        eventQueue.mergeIn(addBanana);
    }

}
