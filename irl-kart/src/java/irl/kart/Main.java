package irl.kart;

import irl.fw.engine.engine.Engine;
import irl.fw.engine.events.EngineEvent;
import irl.kart.beacon.impl.SwingKartBeacon;
import irl.kart.collisions.KartCollisionResolver;
import irl.fw.engine.engine.EngineBuilder;
import irl.kart.phases.HardcodedCourseBuilderPhase;
import irl.kart.phases.KartDetectionPhase;
import irl.kart.renderer.SwingRenderer;
import irl.util.concurrent.ParallelRunnable;
import irl.util.concurrent.SequentialRunnable;
import irl.util.reactiveio.EventQueue;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/1/15
 */
public class Main {

    public static void main(String[] args) throws Exception {
        //create an event queue for kart events
        EventQueue<EngineEvent> kartEventQueue = new EventQueue<>();

        //create the beacon and renderer
        SwingRenderer renderer = new SwingRenderer();
        SwingKartBeacon beacon = new SwingKartBeacon(renderer.getPanel());

        //create the engine
        Engine engine = new EngineBuilder()
            .extraEvents(kartEventQueue.get())
            .collisions(new KartCollisionResolver())
            .renderer(renderer)
            .build();

        //start the engine and world
        new ParallelRunnable(
            true, engine, renderer, beacon
        ).run();

        //start running the different phases
        new SequentialRunnable(
            new KartDetectionPhase(kartEventQueue, beacon),
            new HardcodedCourseBuilderPhase(kartEventQueue)
        ).run();
    }

}
