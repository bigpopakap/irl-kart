package irl.kart.collisions;

import irl.fw.engine.collisions.CollisionResolver;
import irl.fw.engine.entity.EntityInstance;
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
            EntityInstance kart = collision.getType(Kart.class);
            EntityInstance shell = collision.getType(Shell.class);

            System.out.println(String.format("1) Collision between %s and %s", kart.getEntity(), shell.getEntity()));
            System.out.println(String.format("2) Collision between %s and %s", collision.getEntity1().getEntity(), collision.getEntity2().getEntity()));

            eventQueue.mergeIn(Main.addShells(1));
        }
    }

}
