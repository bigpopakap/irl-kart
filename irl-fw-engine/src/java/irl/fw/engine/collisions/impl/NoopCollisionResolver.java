package irl.fw.engine.collisions.impl;

import irl.fw.engine.collisions.CollisionResolver;
import irl.fw.engine.events.EntityCollision;
import irl.fw.engine.events.EngineEvent;

import java.util.Collections;
import java.util.List;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/1/15
 */
public class NoopCollisionResolver implements CollisionResolver {

    @Override
    public List<EngineEvent> onCollision(EntityCollision collision) {
        return Collections.emptyList();
    }

}
