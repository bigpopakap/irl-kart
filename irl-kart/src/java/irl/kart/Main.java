package irl.kart;

import irl.fw.engine.engine.Engine;
import irl.kart.entities.TestEntity;
import irl.kart.world.SwingWorld;
import irl.fw.engine.events.AddEntity;
import irl.fw.engine.engine.EngineBuilder;
import irl.util.concurrent.ParallelRunnable;

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
        //set up a process to add new entity whenever a new kart is detected
        engine.getEventQueue().mergeIn(
            world.updates()
                .distinct(update -> update.getExternalId())
                .map(update -> new AddEntity(
                    new TestEntity(update.getExternalId(), world),
                    update.getState()
                ))
        );

        //start the engine and world
        ParallelRunnable runAll = new ParallelRunnable(
            true, world, engine
        );
        runAll.run();
    }

}
