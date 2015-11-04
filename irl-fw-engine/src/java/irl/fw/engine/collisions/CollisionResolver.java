package irl.fw.engine.collisions;

import irl.fw.engine.events.Collision;
import irl.fw.engine.events.PhysicalEvent;

import java.util.List;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 10/29/15
 */
public interface CollisionResolver {

    List<PhysicalEvent> onCollision(Collision collision);

}
