package irl.fw.engine.graphics.irl.fw.engine.graphics.impl.server;

import irl.fw.engine.graphics.Renderer;
import irl.fw.engine.world.World;
import irl.util.callbacks.Callback;
import irl.util.callbacks.Callbacks;
import irl.util.concurrent.StoppableRunnable;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 7/14/17
 */
public class ServerRenderer implements Renderer, StoppableRunnable {

    private boolean isStopped = false;
    private final Callbacks onStop = new Callbacks();

    @Override
    public void stop() {
        if (!isStopped()) {
            isStopped = true;
            onStop.run();
        }
    }

    @Override
    public boolean isStopped() {
        return isStopped;
    }

    @Override
    public String onStop(Callback callback) {
        return onStop.add(callback);
    }

    @Override
    public void run() {
        // do nothing, yet
        // TODO run a server
    }

    @Override
    public void render(World world, long timeSinceLastUpdate) {
        // TODO use Gson
        System.out.println(world.toJSON());
    }

}
