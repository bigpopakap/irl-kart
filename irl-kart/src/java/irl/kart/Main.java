package irl.kart;

import irl.fw.engine.engine.Engine;
import irl.fw.engine.events.EngineEvent;
import irl.fw.engine.graphics.Renderers;
import irl.fw.engine.graphics.irl.fw.engine.graphics.impl.server.ServerRenderer;
import irl.kart.beacon.impl.swing.SwingKartBeacon;
import irl.fw.engine.engine.EngineBuilder;
import irl.kart.phases.HardcodedCourseBuilderPhase;
import irl.kart.phases.KartDetectionPhase;
import irl.fw.engine.graphics.impl.swing.SwingRenderer;
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

        //set up the Swing inputs and visualizer
        SwingRenderer swingRenderer = new SwingRenderer();
        SwingKartBeacon swingBeacon = new SwingKartBeacon(swingRenderer.getPanel());

        //set up the JSON server
        ServerRenderer serverRenderer = new ServerRenderer();

        Engine engine = new EngineBuilder()
            .extraEvents(kartEventQueue.get())
            .renderer(new Renderers(swingRenderer, serverRenderer))
            .build();

        //start the engine and world
        new ParallelRunnable(
            true,
            engine,
            swingRenderer,
            swingBeacon,
            serverRenderer
        ).run();

        //start running the different phases
        new SequentialRunnable(
            new KartDetectionPhase(kartEventQueue, swingBeacon),
            new HardcodedCourseBuilderPhase(kartEventQueue)
        ).run();
    }

}
