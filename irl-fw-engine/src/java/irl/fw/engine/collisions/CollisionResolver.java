package irl.fw.engine.collisions;

import irl.fw.engine.events.EntityCollision;
import irl.fw.engine.events.EngineEvent;

import java.util.List;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 10/29/15
 */
public interface CollisionResolver {

    List<EngineEvent> onCollision(EntityCollision collision);

}
