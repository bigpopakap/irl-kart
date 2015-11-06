package irl.fw.engine.graphics;

import irl.fw.engine.world.World;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/3/15
 */
public interface Renderer {

    void render(World world, long timeSinceLastUpdate);

}
