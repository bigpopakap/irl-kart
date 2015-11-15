package irl.kart.renderer;

import irl.fw.engine.graphics.Renderer;
import irl.fw.engine.world.World;
import irl.util.callbacks.Callback;
import irl.util.concurrent.StoppableRunnable;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/14/15
 */
public class SwingRenderer implements Renderer, StoppableRunnable {

    @Override
    public void render(World world, long timeSinceLastUpdate) {

    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isStopped() {
        return false;
    }

    @Override
    public String onStop(Callback callback) {
        return null;
    }

    @Override
    public void run() {

    }

}
