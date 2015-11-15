package irl.kart;

import irl.fw.engine.engine.Engine;
import irl.fw.engine.events.EngineEvent;
import irl.kart.beacon.impl.SwingKartBeacon;
import irl.kart.collisions.KartCollisionResolver;
import irl.fw.engine.engine.EngineBuilder;
import irl.kart.engine.Initializer;
import irl.kart.renderer.SwingRenderer;
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
        SwingRenderer renderer = new SwingRenderer();
        SwingKartBeacon beacon = new SwingKartBeacon(renderer.getPanel());
        Initializer initializer = new Initializer(kartEventQueue, beacon);

        //create the engine
        Engine engine = new EngineBuilder()
            .extraEvents(kartEventQueue.get())
            .collisions(new KartCollisionResolver())
            .renderer(renderer)
            .build();

        //start the engine and world
        //FIXME race condition here... engine needs to start first
        ParallelRunnable runAll = new ParallelRunnable(
            true, engine, renderer, beacon
        );
        runAll.run();

        initializer.init();
    }

}
