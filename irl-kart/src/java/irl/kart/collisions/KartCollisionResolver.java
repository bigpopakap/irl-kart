package irl.kart.collisions;

import irl.fw.engine.collisions.CollisionResolver;
import irl.fw.engine.events.EngineEvent;
import irl.fw.engine.events.EntityCollision;
import irl.kart.Main;
import irl.kart.entities.Kart;
import irl.kart.entities.Shell;
import irl.util.reactiveio.Pipe;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/11/15
 */
public class KartCollisionResolver implements CollisionResolver {

    private final Pipe<EngineEvent> eventQueue;

    public KartCollisionResolver(Pipe<EngineEvent> eventQueue) {
        this.eventQueue = eventQueue;
    }

    @Override
    public boolean onBeforeCollision(EntityCollision collision) {
        return true;
    }

    @Override
    public void onCollision(EntityCollision collision) {
        if (collision.isBetween(Kart.class, Shell.class)) {
            eventQueue.mergeIn(Main.addShells(1));
        }
    }

}
