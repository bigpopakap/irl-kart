package irl.kart;

import irl.fw.physics.runner.Looper;
import irl.fw.physics.world.World;
import irl.fw.physics.world.WorldBuilder;
import irl.kart.bodies.TestBody;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/1/15
 */
public class Main {

    public static void main(String[] args) throws Exception {
        //build the world
        World world = new WorldBuilder()
                .addBody(new TestBody())
                .addBody(new TestBody())
                .build();

        //start the world in a new thread
        Looper worldLoop = new Looper(world);
        Thread worldThread = new Thread(worldLoop);
        worldThread.start();

        //TODO start an input thread

        //kill it after a little bit
        Thread.sleep(5000);
        worldLoop.stop();
    }

}
