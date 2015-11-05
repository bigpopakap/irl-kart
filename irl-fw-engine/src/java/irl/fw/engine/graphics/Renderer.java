package irl.fw.engine.graphics;

import irl.fw.engine.entity.EntityInstance;

import java.util.Collection;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/3/15
 */
public interface Renderer {

    void render(Collection<EntityInstance> entities, long timeSinceLastUpdate);

}
