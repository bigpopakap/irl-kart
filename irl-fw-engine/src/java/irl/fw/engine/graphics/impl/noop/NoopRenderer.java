package irl.fw.engine.graphics.impl.noop;

import irl.fw.engine.graphics.Renderer;
import irl.fw.engine.world.World;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/3/15
 */
public class NoopRenderer implements Renderer {

    @Override
    public void render(World world, long timeStep) {
        //do nothing
    }

}