package irl.kart;

import irl.fw.physics.bodies.IRLBody;
import irl.fw.physics.events.UpdateBody;
import irl.fw.physics.runner.Looper;
import irl.fw.physics.world.World;
import irl.fw.physics.world.WorldBuilder;
import rx.Observable;

import java.awt.*;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/1/15
 */
public class Main {

    private static class EmptyBody extends IRLBody {

        public Shape getShape() {
            return null;
        }

        @Override
        public Observable<UpdateBody> updates() {
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

        //TODO start an input thread

        //kill it after a little bit
        Thread.sleep(5000);
        worldLoop.stop();
    }

}
