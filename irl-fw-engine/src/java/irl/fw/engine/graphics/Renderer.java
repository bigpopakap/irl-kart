package irl.fw.engine.graphics;

import irl.fw.engine.bodies.BodyInstance;

import java.util.Collection;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/3/15
 */
public interface Renderer {

    void render(Collection<BodyInstance> bodies, long timeSinceLastUpdate);

}
