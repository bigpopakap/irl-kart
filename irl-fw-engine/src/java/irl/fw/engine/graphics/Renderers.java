package irl.fw.engine.graphics;

import irl.fw.engine.world.World;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/15/15
 */
public class Renderers implements Renderer {

    private final Renderer[] renderers;

    public Renderers(Renderer... renderers) {
        this.renderers = renderers;
    }

    @Override
    public void render(World world, long timeSinceLastUpdate) {
        for (Renderer renderer : renderers) {
            renderer.render(world, timeSinceLastUpdate);
        }
    }

}
