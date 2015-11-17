package irl.kart.phases;

import irl.fw.engine.entity.state.EntityStateBuilder;
import irl.fw.engine.events.AddEntity;
import irl.fw.engine.events.EngineEvent;
import irl.fw.engine.geometry.Angle;
import irl.fw.engine.geometry.ImmutableShape;
import irl.fw.engine.geometry.Vector2D;
import irl.kart.entities.Wall;
import irl.kart.entities.ItemBoxPedestal;
import irl.util.concurrent.SynchronousRunnable;
import irl.util.reactiveio.EventQueue;
import rx.Observable;

import java.awt.geom.Rectangle2D;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/15/15
 */
public class HardcodedCourseBuilderPhase extends SynchronousRunnable {

    private static final Rectangle2D WORLD_BOUNDS = new Rectangle2D.Double(
        50, 50, 800, 600
    );
    private static final Vector2D BOTTOM_LEFT = new Vector2D(WORLD_BOUNDS.getMinX(), WORLD_BOUNDS.getMinY());
    private static final double WALL_THICKNESS = 20;

    private final EventQueue<EngineEvent> eventQueue;

    public HardcodedCourseBuilderPhase(EventQueue<EngineEvent> eventQueue) {
        this.eventQueue = eventQueue;
    }

    @Override
    protected void doRunSynchronous() {
        eventQueue.mergeIn(addWalls());
        eventQueue.mergeIn(addItemBoxes());
    }

    private Observable<AddEntity> addWalls() {
        return Observable.from(new AddEntity[] {

                //left wall
                new AddEntity(entityConfig -> new Wall(
                        entityConfig,
                        new EntityStateBuilder().defaults()
                                .shape(new ImmutableShape(ImmutableShape.Type.RECTANGLE,
                                        new Rectangle2D.Double(0, 0, WALL_THICKNESS, WORLD_BOUNDS.getHeight())))
                                .center(new Vector2D(WALL_THICKNESS / 2, WORLD_BOUNDS.getHeight() / 2)
                                        .add(BOTTOM_LEFT))
                                .build()
                )),

                //right wall
                new AddEntity(entityConfig -> new Wall(
                        entityConfig,
                        new EntityStateBuilder().defaults()
                                .shape(new ImmutableShape(ImmutableShape.Type.RECTANGLE,
                                        new Rectangle2D.Double(0, 0, WALL_THICKNESS, WORLD_BOUNDS.getHeight())))
                                .center(new Vector2D(WORLD_BOUNDS.getWidth() - WALL_THICKNESS / 2, WORLD_BOUNDS.getHeight() / 2)
                                        .add(BOTTOM_LEFT))
                                .build()
                )),

                //top wall
                new AddEntity(entityConfig -> new Wall(
                        entityConfig,
                        new EntityStateBuilder().defaults()
                                .shape(new ImmutableShape(ImmutableShape.Type.RECTANGLE,
                                        new Rectangle2D.Double(0, 0, WALL_THICKNESS, WORLD_BOUNDS.getWidth())))
                                .rotation(Angle.deg(90))
                                .center(new Vector2D(WORLD_BOUNDS.getWidth()/2, WORLD_BOUNDS.getHeight() - WALL_THICKNESS/2)
                                        .add(BOTTOM_LEFT))
                                .build()
                )),

                //bottom wall
                new AddEntity(entityConfig -> new Wall(
                        entityConfig,
                        new EntityStateBuilder().defaults()
                                .shape(new ImmutableShape(ImmutableShape.Type.RECTANGLE,
                                        new Rectangle2D.Double(0, 0, WALL_THICKNESS, WORLD_BOUNDS.getWidth())))
                                .rotation(Angle.deg(90))
                                .center(new Vector2D(WORLD_BOUNDS.getWidth()/2, WALL_THICKNESS/2)
                                            .add(BOTTOM_LEFT))
                                .build()
                ))

        });
    }

    private Observable<AddEntity> addItemBoxes() {
        final double INSET = 6*WALL_THICKNESS;

        return Observable.from(new AddEntity[] {

                //top left
                new AddEntity(entityConfig -> new ItemBoxPedestal(
                        entityConfig,
                        new EntityStateBuilder().defaults()
                                .center(new Vector2D(INSET, WORLD_BOUNDS.getHeight() - INSET)
                                            .add(BOTTOM_LEFT))
                                .shape(ItemBoxPedestal.SHAPE)
                                .build(),
                        eventQueue
                )),

                //top right
                new AddEntity(entityConfig -> new ItemBoxPedestal(
                        entityConfig,
                        new EntityStateBuilder().defaults()
                                .center(new Vector2D(WORLD_BOUNDS.getWidth() - INSET, WORLD_BOUNDS.getHeight() - INSET)
                                        .add(BOTTOM_LEFT))
                                .shape(ItemBoxPedestal.SHAPE)
                                .build(),
                        eventQueue
                )),

                //bottom left
                new AddEntity(entityConfig -> new ItemBoxPedestal(
                        entityConfig,
                        new EntityStateBuilder().defaults()
                                .center(new Vector2D(INSET, INSET)
                                        .add(BOTTOM_LEFT))
                                .shape(ItemBoxPedestal.SHAPE)
                                .build(),
                        eventQueue
                )),

                //bottom right
                new AddEntity(entityConfig -> new ItemBoxPedestal(
                        entityConfig,
                        new EntityStateBuilder().defaults()
                                .center(new Vector2D(WORLD_BOUNDS.getWidth() - INSET, INSET)
                                        .add(BOTTOM_LEFT))
                                .shape(ItemBoxPedestal.SHAPE)
                                .build(),
                        eventQueue
                ))

        });
    }

}
