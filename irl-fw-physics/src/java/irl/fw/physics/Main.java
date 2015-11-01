package irl.fw.physics;

import irl.fw.physics.bodies.VirtualBody;
import irl.fw.physics.runner.Looper;
import irl.fw.physics.world.World;
import irl.fw.physics.world.WorldBuilder;
import rx.Observable;

import java.awt.*;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 10/29/15
 */
public class Main {

    private static class EmptyBody extends VirtualBody {

        @Override
        public Shape getShape() {
            return null;
        }

    }

    public static void main(String[] args) throws Exception {
        //build the world
        World world = new WorldBuilder()
                            .addBody(new EmptyBody())
                            .addBody(new EmptyBody())
                            .build();

        //start the world in a new thread
        Looper worldLoop = new Looper(world);
        Thread worldThread = new Thread(worldLoop);
        worldThread.start();

        //kill it after a little bit
        Thread.sleep(1000);
        worldLoop.stop();

        //start it again
        System.out.println("stopping");
        Thread.sleep(2000);
        System.out.println("starting again");
        worldThread = new Thread(worldLoop);
        worldThread.start();
        Thread.sleep(1000);
        worldLoop.stop();
    }

}