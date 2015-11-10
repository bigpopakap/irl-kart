package irl.kart;

import irl.fw.engine.engine.Engine;
import irl.fw.engine.entity.state.EntityStateBuilder;
import irl.fw.engine.geometry.Angle;
import irl.fw.engine.geometry.ImmutableShape;
import irl.fw.engine.geometry.Vector2D;
import irl.kart.entities.Kart;
import irl.kart.world.SwingWorld;
import irl.fw.engine.events.AddEntity;
import irl.fw.engine.engine.EngineBuilder;
import irl.util.concurrent.ParallelRunnable;

import java.awt.geom.Rectangle2D;

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

        final ImmutableShape DEFAULT_KART_SHAPE = new ImmutableShape(
            new Rectangle2D.Double(0, 0, 10, 20)
        );
        //FIXME this should move somewhere more generic
        //set up a process to addEntity new entity whenever a new kart is detected
        engine.getEventQueue().mergeIn(
                world.updates()
                        .distinct(update -> update.getExternalId())
                        .map(update -> new AddEntity(
                                new Kart(update.getExternalId(), world),
                                new EntityStateBuilder()
                                    .shape(DEFAULT_KART_SHAPE)
                                    .rotation(Angle.deg(0))
                                    .center(new Vector2D(50, 50))
                                    .velocity(new Vector2D(0, 0))
                                    .build()
                        ))
        );

        //start the engine and world
        ParallelRunnable runAll = new ParallelRunnable(
            true, world, engine
        );
        runAll.run();
    }

}
