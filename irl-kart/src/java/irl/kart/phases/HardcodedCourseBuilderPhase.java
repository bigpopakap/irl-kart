package irl.kart.phases;

import irl.fw.engine.entity.state.EntityStateBuilder;
import irl.fw.engine.events.AddEntity;
import irl.fw.engine.events.EngineEvent;
import irl.fw.engine.geometry.Angle;
import irl.fw.engine.geometry.ImmutableShape;
import irl.fw.engine.geometry.Vector2D;
import irl.kart.entities.Desert;
import irl.kart.entities.Grass;
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

    private static final double WALL_THICKNESS = 15;
    private static final Rectangle2D WORLD_BOUNDS = new Rectangle2D.Double(
        -10, -10, 400, 400
    );
    private static final Rectangle2D INNER_WALL_BOUNDS = new Rectangle2D.Double(
        WORLD_BOUNDS.getMinX() + .25*WORLD_BOUNDS.getWidth(),
        WORLD_BOUNDS.getMinY() + .25*WORLD_BOUNDS.getHeight(),
        .5*WORLD_BOUNDS.getWidth(),
        .5*WORLD_BOUNDS.getHeight()
    );
    private static final Rectangle2D GRASS_BOUNDS = new Rectangle2D.Double(
        INNER_WALL_BOUNDS.getMinX(),
        WORLD_BOUNDS.getMinY() + WALL_THICKNESS,
        INNER_WALL_BOUNDS.getWidth(),
        ((WORLD_BOUNDS.getHeight() - INNER_WALL_BOUNDS.getHeight()) / 2) - WALL_THICKNESS
    );
    private static final Rectangle2D DESERT_BOUNDS = new Rectangle2D.Double(
        INNER_WALL_BOUNDS.getMinX(),
        (WORLD_BOUNDS.getHeight() + INNER_WALL_BOUNDS.getHeight()) / 2,
        INNER_WALL_BOUNDS.getWidth(),
        (WORLD_BOUNDS.getHeight() - INNER_WALL_BOUNDS.getHeight()) / 2
    );
    private static final double ITEM_BOX_INSET = WORLD_BOUNDS.getWidth() / 8.0;

    private final EventQueue<EngineEvent> eventQueue;

    public HardcodedCourseBuilderPhase(EventQueue<EngineEvent> eventQueue) {
        this.eventQueue = eventQueue;
    }

    @Override
    protected void doRunSynchronous() {
        eventQueue.mergeIn(addGrass(GRASS_BOUNDS));
        eventQueue.mergeIn(addDesert(DESERT_BOUNDS));
        eventQueue.mergeIn(addWalls(WORLD_BOUNDS));
        eventQueue.mergeIn(addWalls(INNER_WALL_BOUNDS));
        eventQueue.mergeIn(addItemBoxes(WORLD_BOUNDS));
    }

    private Observable<AddEntity> addWalls(Rectangle2D bounds) {
        final Vector2D bottomLeft = new Vector2D(bounds.getMinX(), bounds.getMinY());

        return Observable.from(new AddEntity[] {

                //left wall
                new AddEntity(entityConfig -> new Wall(
                        entityConfig,
                        new EntityStateBuilder().defaults()
                                .shape(new ImmutableShape(ImmutableShape.Type.RECTANGLE,
                                        new Rectangle2D.Double(0, 0, WALL_THICKNESS, bounds.getHeight())))
                                .center(new Vector2D(WALL_THICKNESS / 2, bounds.getHeight() / 2)
                                        .add(bottomLeft))
                                .build()
                )),

                //right wall
                new AddEntity(entityConfig -> new Wall(
                        entityConfig,
                        new EntityStateBuilder().defaults()
                                .shape(new ImmutableShape(ImmutableShape.Type.RECTANGLE,
                                        new Rectangle2D.Double(0, 0, WALL_THICKNESS, bounds.getHeight())))
                                .center(new Vector2D(bounds.getWidth() - WALL_THICKNESS / 2, bounds.getHeight() / 2)
                                        .add(bottomLeft))
                                .build()
                )),

                //top wall
                new AddEntity(entityConfig -> new Wall(
                        entityConfig,
                        new EntityStateBuilder().defaults()
                                .shape(new ImmutableShape(ImmutableShape.Type.RECTANGLE,
                                        new Rectangle2D.Double(0, 0, WALL_THICKNESS, bounds.getWidth())))
                                .rotation(Angle.deg(90))
                                .center(new Vector2D(bounds.getWidth()/2, bounds.getHeight() - WALL_THICKNESS/2)
                                        .add(bottomLeft))
                                .build()
                )),

                //bottom wall
                new AddEntity(entityConfig -> new Wall(
                        entityConfig,
                        new EntityStateBuilder().defaults()
                                .shape(new ImmutableShape(ImmutableShape.Type.RECTANGLE,
                                        new Rectangle2D.Double(0, 0, WALL_THICKNESS, bounds.getWidth())))
                                .rotation(Angle.deg(90))
                                .center(new Vector2D(bounds.getWidth()/2, WALL_THICKNESS/2)
                                            .add(bottomLeft))
                                .build()
                ))

        });
    }

    private Observable<AddEntity> addGrass(Rectangle2D bounds) {
        return Observable.from(new AddEntity[] {
            new AddEntity(entityConfig -> new Grass(
                entityConfig,
                new EntityStateBuilder().defaults()
                    .shape(new ImmutableShape(
                        ImmutableShape.Type.RECTANGLE,
                        bounds
                    ))
                    .center(new Vector2D(bounds.getCenterX(), bounds.getCenterY()))
                    .build()
            ))
        });
    }

    private Observable<AddEntity> addDesert(Rectangle2D bounds) {
        return Observable.from(new AddEntity[] {
            new AddEntity(entityConfig -> new Desert(
                entityConfig,
                new EntityStateBuilder().defaults()
                    .shape(new ImmutableShape(
                            ImmutableShape.Type.RECTANGLE,
                            bounds
                    ))
                    .center(new Vector2D(bounds.getCenterX(), bounds.getCenterY()))
                    .build()
            ))
        });
    }

    private Observable<AddEntity> addItemBoxes(Rectangle2D bounds) {
        final Vector2D bottomLeft = new Vector2D(bounds.getMinX(), bounds.getMinY());

        return Observable.from(new AddEntity[] {

                //top left
                new AddEntity(entityConfig -> new ItemBoxPedestal(
                        entityConfig,
                        new EntityStateBuilder().defaults()
                                .center(new Vector2D(ITEM_BOX_INSET, bounds.getHeight() - ITEM_BOX_INSET)
                                            .add(bottomLeft))
                                .shape(ItemBoxPedestal.SHAPE)
                                .build(),
                        eventQueue
                )),

                //top right
                new AddEntity(entityConfig -> new ItemBoxPedestal(
                        entityConfig,
                        new EntityStateBuilder().defaults()
                                .center(new Vector2D(bounds.getWidth() - ITEM_BOX_INSET, bounds.getHeight() - ITEM_BOX_INSET)
                                        .add(bottomLeft))
                                .shape(ItemBoxPedestal.SHAPE)
                                .build(),
                        eventQueue
                )),

                //bottom left
                new AddEntity(entityConfig -> new ItemBoxPedestal(
                        entityConfig,
                        new EntityStateBuilder().defaults()
                                .center(new Vector2D(ITEM_BOX_INSET, ITEM_BOX_INSET)
                                        .add(bottomLeft))
                                .shape(ItemBoxPedestal.SHAPE)
                                .build(),
                        eventQueue
                )),

                //bottom right
                new AddEntity(entityConfig -> new ItemBoxPedestal(
                        entityConfig,
                        new EntityStateBuilder().defaults()
                                .center(new Vector2D(bounds.getWidth() - ITEM_BOX_INSET, ITEM_BOX_INSET)
                                        .add(bottomLeft))
                                .shape(ItemBoxPedestal.SHAPE)
                                .build(),
                        eventQueue
                ))

        });
    }

}
