package irl.kart;

import irl.fw.engine.engine.Engine;
import irl.fw.engine.entity.state.EntityStateBuilder;
import irl.fw.engine.geometry.Angle;
import irl.fw.engine.geometry.ImmutableShape;
import irl.fw.engine.geometry.Vector2D;
import irl.kart.entities.Kart;
import irl.kart.entities.Shell;
import irl.kart.entities.Wall;
import irl.kart.world.SwingWorld;
import irl.fw.engine.events.AddEntity;
import irl.fw.engine.engine.EngineBuilder;
import irl.util.concurrent.ParallelRunnable;
import rx.Observable;

import java.awt.geom.Rectangle2D;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/1/15
 */
//TODO it's time to put this stuff into its own class
public class Main {

    private static final double WORLD_WIDTH = 600;
    private static final double WORLD_HEIGHT = 450;

    private static final double WALL_THICKNESS = 20;

    private static final int NUM_SHELLS = 20;

    public static void main(String[] args) throws Exception {
        //create the beacon and renderer
        SwingWorld world = new SwingWorld("kart1", "kart2");

        //create the engine
        Engine engine = new EngineBuilder()
            .renderer(world)
            .build();

        //start the engine and world
        ParallelRunnable runAll = new ParallelRunnable(
            true, world, engine
        );
        runAll.run();

        //add the walls
        engine.getEventQueue().mergeIn(
            addWalls(new Rectangle2D.Double(0, 0, WORLD_WIDTH, WORLD_HEIGHT))
        );

        //add an initial green shell
        engine.getEventQueue().mergeIn(addShells(NUM_SHELLS));

        //FIXME this should move somewhere more generic
        //set up a process to addEntity new entity whenever a new kart is detected
        engine.getEventQueue().mergeIn(
            world.updates()
                .distinct(update -> update.getExternalId())
                .map(update -> new AddEntity(
                        new Kart(update.getExternalId(), world),
                        new EntityStateBuilder()
                                .shape(Kart.SHAPE)
                                .rotation(Angle.deg(0))
                                .center(new Vector2D(50, 50))
                                .velocity(new Vector2D(0, 0))
                                .build()
                ))
        );
    }

    private static Observable<AddEntity> addShells(int numShells) {
        AddEntity[] events = new AddEntity[numShells];

        for (int i = 0; i < numShells; i++) {
            events[i] = new AddEntity(
                new Shell(),
                new EntityStateBuilder()
                        .shape(Shell.SHAPE)
                        .rotation(Angle.deg(0))
                        .center(new Vector2D(2*WALL_THICKNESS + (WORLD_WIDTH - 4*WALL_THICKNESS) * Math.random(),
                                             2*WALL_THICKNESS + (WORLD_HEIGHT - 4*WALL_THICKNESS) * Math.random()))
                        .velocity(new Vector2D(40*Math.random(), 40*Math.random()))
                        .build()
            );
        }

        return Observable.from(events);
    }

    private static Observable<AddEntity> addWalls(Rectangle2D worldBounds) {
        return Observable.from(new AddEntity[] {

            //left wall
            new AddEntity(
                new Wall(),
                new EntityStateBuilder()
                    .shape(new ImmutableShape(ImmutableShape.Type.RECTANGLE,
                            new Rectangle2D.Double(0, 0, WALL_THICKNESS, worldBounds.getHeight())))
                    .rotation(Angle.deg(0))
                    .center(new Vector2D(WALL_THICKNESS / 2, worldBounds.getHeight() / 2))
                    .velocity(new Vector2D(0, 0))
                    .build()
            ),

            //right wall
            new AddEntity(
                    new Wall(),
                    new EntityStateBuilder()
                            .shape(new ImmutableShape(ImmutableShape.Type.RECTANGLE,
                                    new Rectangle2D.Double(0, 0, WALL_THICKNESS, worldBounds.getHeight())))
                            .rotation(Angle.deg(0))
                            .center(new Vector2D(worldBounds.getWidth() - WALL_THICKNESS / 2, worldBounds.getHeight() / 2))
                            .velocity(new Vector2D(0, 0))
                            .build()
            ),

            //top wall
            new AddEntity(
                    new Wall(),
                    new EntityStateBuilder()
                            .shape(new ImmutableShape(ImmutableShape.Type.RECTANGLE,
                                    new Rectangle2D.Double(0, 0, WALL_THICKNESS, worldBounds.getWidth())))
                            .rotation(Angle.deg(90))
                            .center(new Vector2D(worldBounds.getWidth()/2, worldBounds.getHeight() - WALL_THICKNESS/2))
                            .velocity(new Vector2D(0, 0))
                            .build()
            ),

            //bottom wall
            new AddEntity(
                    new Wall(),
                    new EntityStateBuilder()
                            .shape(new ImmutableShape(ImmutableShape.Type.RECTANGLE,
                                    new Rectangle2D.Double(0, 0, WALL_THICKNESS, worldBounds.getWidth())))
                            .rotation(Angle.deg(90))
                            .center(new Vector2D(worldBounds.getWidth()/2, WALL_THICKNESS/2))
                            .velocity(new Vector2D(0, 0))
                            .build()
            ),

        });
    }

}
