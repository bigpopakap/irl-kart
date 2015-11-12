package irl.kart;

import irl.fw.engine.engine.Engine;
import irl.fw.engine.events.EngineEvent;
import irl.kart.collisions.KartCollisionResolver;
import irl.kart.world.SwingWorld;
import irl.fw.engine.engine.EngineBuilder;
import irl.util.concurrent.ParallelRunnable;
import irl.util.reactiveio.Pipe;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/1/15
 */
//TODO it's time to put this stuff into its own class
public class Main {

    public static void main(String[] args) throws Exception {
        //create an event queue for kart events
        Pipe<EngineEvent> kartEventQueue = new Pipe<>();

        //create the beacon and renderer
        SwingWorld world = new SwingWorld(kartEventQueue, "kart1", "kart2");

        //create the engine
        Engine engine = new EngineBuilder()
            .extraEvents(kartEventQueue.get())
            .collisions(new KartCollisionResolver())
            .renderer(world)
            .build();

        //start the engine and world
        ParallelRunnable runAll = new ParallelRunnable(
            true, engine, world
        );
        runAll.run();
    }

}
