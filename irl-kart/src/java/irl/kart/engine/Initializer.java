package irl.kart.engine;

import irl.fw.engine.entity.state.EntityStateBuilder;
import irl.fw.engine.events.AddEntity;
import irl.fw.engine.events.EngineEvent;
import irl.fw.engine.geometry.Angle;
import irl.fw.engine.geometry.ImmutableShape;
import irl.fw.engine.geometry.Vector2D;
import irl.kart.beacon.KartBeacon;
import irl.kart.entities.Kart;
import irl.kart.entities.Wall;
import irl.kart.entities.items.ItemBoxPedestal;
import irl.kart.events.beacon.KartStateUpdate;
import irl.util.reactiveio.EventQueue;
import rx.Observable;

import java.awt.geom.Rectangle2D;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/14/15
 */
public class Initializer {

    private static final Rectangle2D WORLD_BOUNDS = new Rectangle2D.Double(
        0, 0, 1000, 500
    );

    private static final double WALL_THICKNESS = 20;

    private final EventQueue<EngineEvent> eventQueue;
    private final KartBeacon beacon;

    public Initializer(EventQueue<EngineEvent> eventQueue, KartBeacon beacon) {
        this.eventQueue = eventQueue;
        this.beacon = beacon;
    }

    public void init() {
        //add walls and add karts when they are first seen
        eventQueue.mergeIn(addWalls());
        eventQueue.mergeIn(addItemBoxes());
        eventQueue.mergeIn(addNewKarts());
    }

    private Observable<AddEntity> addWalls() {
        return Observable.from(new AddEntity[] {

                //left wall
                new AddEntity(entityConfig -> new Wall(
                        entityConfig,
                        new EntityStateBuilder().defaults()
                                .shape(new ImmutableShape(ImmutableShape.Type.RECTANGLE,
                                        new Rectangle2D.Double(0, 0, WALL_THICKNESS, WORLD_BOUNDS.getHeight())))
                                .center(new Vector2D(WALL_THICKNESS / 2, WORLD_BOUNDS.getHeight() / 2))
                                .build()
                )),

                //right wall
                new AddEntity(entityConfig -> new Wall(
                        entityConfig,
                        new EntityStateBuilder().defaults()
                                .shape(new ImmutableShape(ImmutableShape.Type.RECTANGLE,
                                        new Rectangle2D.Double(0, 0, WALL_THICKNESS, WORLD_BOUNDS.getHeight())))
                                .center(new Vector2D(WORLD_BOUNDS.getWidth() - WALL_THICKNESS / 2, WORLD_BOUNDS.getHeight() / 2))
                                .build()
                )),

                //top wall
                new AddEntity(entityConfig -> new Wall(
                        entityConfig,
                        new EntityStateBuilder().defaults()
                                .shape(new ImmutableShape(ImmutableShape.Type.RECTANGLE,
                                        new Rectangle2D.Double(0, 0, WALL_THICKNESS, WORLD_BOUNDS.getWidth())))
                                .rotation(Angle.deg(90))
                                .center(new Vector2D(WORLD_BOUNDS.getWidth()/2, WORLD_BOUNDS.getHeight() - WALL_THICKNESS/2))
                                .build()
                )),

                //bottom wall
                new AddEntity(entityConfig -> new Wall(
                        entityConfig,
                        new EntityStateBuilder().defaults()
                                .shape(new ImmutableShape(ImmutableShape.Type.RECTANGLE,
                                        new Rectangle2D.Double(0, 0, WALL_THICKNESS, WORLD_BOUNDS.getWidth())))
                                .rotation(Angle.deg(90))
                                .center(new Vector2D(WORLD_BOUNDS.getWidth()/2, WALL_THICKNESS/2))
                                .build()
                ))

        });
    }

    private Observable<AddEntity> addItemBoxes() {
        final double INSET = 4*WALL_THICKNESS;

        return Observable.from(new AddEntity[] {

                //top left
                new AddEntity(entityConfig -> new ItemBoxPedestal(
                        entityConfig,
                        new EntityStateBuilder().defaults()
                                .center(new Vector2D(INSET, WORLD_BOUNDS.getHeight() - INSET))
                                .shape(ItemBoxPedestal.SHAPE)
                                .build(),
                        eventQueue
                )),

                //top right
                new AddEntity(entityConfig -> new ItemBoxPedestal(
                        entityConfig,
                        new EntityStateBuilder().defaults()
                                .center(new Vector2D(WORLD_BOUNDS.getWidth() - INSET, WORLD_BOUNDS.getHeight() - INSET))
                                .shape(ItemBoxPedestal.SHAPE)
                                .build(),
                        eventQueue
                )),

                //bottom left
                new AddEntity(entityConfig -> new ItemBoxPedestal(
                        entityConfig,
                        new EntityStateBuilder().defaults()
                                .center(new Vector2D(INSET, INSET))
                                .shape(ItemBoxPedestal.SHAPE)
                                .build(),
                        eventQueue
                )),

                //bottom right
                new AddEntity(entityConfig -> new ItemBoxPedestal(
                        entityConfig,
                        new EntityStateBuilder().defaults()
                                .center(new Vector2D(WORLD_BOUNDS.getWidth() - INSET, INSET))
                                .shape(ItemBoxPedestal.SHAPE)
                                .build(),
                        eventQueue
                ))

        });
    }

    private Observable<AddEntity> addNewKarts() {
        return beacon.stream()
                .ofType(KartStateUpdate.class)
                .distinct(update -> update.getKartId())
                .map(update -> new AddEntity(entityConfig -> new Kart(
                        entityConfig,
                        new EntityStateBuilder()
                                .shape(Kart.SHAPE)
                                .rotation(Angle.deg(0))
                                .center(new Vector2D(200, 200))
                                .velocity(new Vector2D(0, 0))
                                .angularVelocity(Angle.deg(0))
                                .build(),
                        update.getKartId(), beacon, eventQueue
                )));
    }

}
