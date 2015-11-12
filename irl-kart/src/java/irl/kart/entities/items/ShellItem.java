package irl.kart.entities.items;

import irl.fw.engine.entity.state.EntityState;
import irl.fw.engine.entity.state.EntityStateBuilder;
import irl.fw.engine.events.AddEntity;
import irl.fw.engine.events.EngineEvent;
import irl.fw.engine.geometry.Vector2D;
import irl.kart.entities.Kart;
import irl.kart.entities.Shell;
import irl.util.reactiveio.Pipe;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/11/15
 */
public class ShellItem implements Item {

    private final Pipe<EngineEvent> eventQueue;

    public ShellItem(Pipe<EngineEvent> eventQueue) {
        this.eventQueue = eventQueue;
    }

    @Override
    public void use(Kart user) {
        EntityState kartState = user.getState();
        Vector2D kartCenter = kartState.getCenter();

        Vector2D shellVelocity = new Vector2D(0, Shell.SPEED).rotate(kartState.getRotation());
        Vector2D shellCenter = kartCenter.add(
                shellVelocity.scaleTo(Kart.KART_LENGTH + 0.25*Shell.SIZE)
        );

        AddEntity addShell = new AddEntity(engineId -> new Shell(
                engineId,
                new EntityStateBuilder().defaults()
                        .shape(Shell.SHAPE)
                        .center(shellCenter)
                        .velocity(shellVelocity)
                        .angularVelocity(Shell.ROTATIONAL_SPEED)
                        .build(),
                user.getKartId(),
                eventQueue
        ));

        eventQueue.mergeIn(addShell);
    }

}
