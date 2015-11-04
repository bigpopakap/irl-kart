package irl.kart;

import irl.fw.engine.engine.Engine;
import irl.kart.world.SwingWorld;
import irl.fw.engine.events.AddBody;
import irl.fw.engine.engine.EngineBuilder;
import irl.kart.bodies.TestBody;
import irl.util.concurrent.ParallelRunnable;
import irl.util.concurrent.StoppableRunnable;

import java.util.concurrent.TimeUnit;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/1/15
 */
public class Main {

    public static void main(String[] args) throws Exception {
        //create the beacon and renderer
        SwingWorld world = new SwingWorld("kart1", "kart2");

        //create the engine
        Engine engine = new EngineBuilder()
            .renderer(world)
            .build();

        //TODO this should move somewhere more generic
        //set up a process to add new bodies whenever a new kart is detected
        engine.getEventQueue().mergeIn(
                world.updates()
                        .distinct(update -> update.getExternalId())
                        .map(update -> new AddBody(
                                new TestBody(update.getExternalId(), world),
                                update.getState()
                        ))
        );

        //start the engine and impl
        ParallelRunnable runAll = new ParallelRunnable(
            world,
            new StoppableRunnable[] { engine },
            1, TimeUnit.MINUTES
        );
        runAll.run();
    }

}
