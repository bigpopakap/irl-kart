package irl.kart.entities.items.actions.holdableitem;

import irl.fw.engine.entity.Entity;
import irl.fw.engine.entity.actions.remove.RemovableEntity;
import irl.fw.engine.entity.factory.EntityFactory;
import irl.fw.engine.entity.state.EntityState;
import irl.fw.engine.entity.state.EntityStateBuilder;
import irl.fw.engine.entity.state.EntityStateUpdate;
import irl.fw.engine.events.AddEntity;
import irl.fw.engine.events.EngineEvent;
import irl.fw.engine.events.UpdateEntity;
import irl.fw.engine.geometry.Angle;
import irl.fw.engine.geometry.Vector2D;
import irl.kart.entities.Kart;
import irl.kart.entities.items.actions.itemuser.ItemUser;
import irl.kart.entities.weapons.Shell;
import irl.util.reactiveio.EventQueue;
import rx.Subscription;

import java.util.Optional;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/15/15
 */
public class HoldableItemAdaptor<T extends Entity & RemovableEntity> {

    private final EventQueue<EngineEvent> eventQueue;
    private final EntityFactory<T> factory;

    private volatile Optional<T> entity = Optional.empty();
    private Subscription userUpdateSubscription = null;

    public HoldableItemAdaptor(EventQueue<EngineEvent> eventQueue, EntityFactory<T> factory) {
        this.eventQueue = eventQueue;
        this.factory = factory;
    }

    public synchronized <U extends Entity & ItemUser> void doHoldItem(U user, double distanceBetweenCenters) {
        if (entity.isPresent()) {
            return;
        }

        //create the new held entity
        EntityFactory<T> factoryWrapper = entityConfig -> {
            T newEntity = factory.create(entityConfig);

            //track that this item is held
            setCreatedEntity(newEntity);

            userUpdateSubscription = user.getStateUpdates().subscribe(userState -> {
                eventQueue.mergeIn(new UpdateEntity(
                        newEntity.getEngineId(),
                        new EntityStateUpdate()
                                .center(calculateHeldItemCenter(userState, distanceBetweenCenters))
                ));
            });

            return newEntity;
        };

        AddEntity addShell = new AddEntity(factoryWrapper);
        eventQueue.mergeIn(addShell);
    }

    public synchronized <T extends Entity & ItemUser> void doUseItem(T user) {
        //TODO use the held entity if there is one

        EntityState userState = user.getState();
        Vector2D kartCenter = userState.getCenter();

        Vector2D shellVelocity = new Vector2D(0, Shell.SPEED).rotate(userState.getRotation());
        Vector2D shellCenter = kartCenter.add(
                shellVelocity.scaleTo(Kart.KART_LENGTH + 0.25*Shell.SIZE)
        );

        AddEntity addShell = new AddEntity(entityConfig -> new Shell(
                entityConfig,
                new EntityStateBuilder().defaults()
                        .shape(Shell.SHAPE)
                        .center(shellCenter)
                        .velocity(shellVelocity)
                        .angularVelocity(Shell.ROTATIONAL_SPEED)
                        .build(),
                user.getEngineId(),
                eventQueue
        ));

        eventQueue.mergeIn(addShell);

        onEntityUsedOrRemoved();
        //TODO fire an event that the item was used
    }

    private void setCreatedEntity(T entity) {
        if (this.entity.isPresent()) {
            throw new IllegalStateException("An item is already held");
        }

        entity.onRemove(this::onEntityUsedOrRemoved);
        //TODO fire an event when the item is removed
        this.entity = Optional.ofNullable(entity);
    }

    private Vector2D calculateHeldItemCenter(EntityState userState, double distanceBetweenCenters) {
        Vector2D userCenter = userState.getCenter();

        Vector2D shellDirection = new Vector2D(0, 1)
                .rotate(userState.getRotation())
                .rotate(Angle.HALF);
        Vector2D shellCenter = userCenter.add(
                shellDirection.scaleTo(distanceBetweenCenters)
        );

        return shellCenter;
    }

    private void onEntityUsedOrRemoved() {
        entity = Optional.empty();
        if (userUpdateSubscription != null) {
            userUpdateSubscription.unsubscribe();
            userUpdateSubscription = null;
        }
    }

}
