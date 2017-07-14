package irl.kart.entities.weapons;

import irl.fw.engine.entity.factory.EntityConfig;
import irl.fw.engine.entity.factory.EntityDisplayConfig;
import irl.fw.engine.entity.state.EntityState;
import irl.fw.engine.events.EngineEvent;
import irl.fw.engine.geometry.ImmutableShape;
import irl.util.reactiveio.EventQueue;

import java.awt.*;
import java.awt.geom.Ellipse2D;


/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/15/15
 */
public class Banana extends WeaponEntity {

    public static final double SIZE = 10;
    public static final ImmutableShape SHAPE = new ImmutableShape(
        ImmutableShape.Type.ELLIPSE,
        new Ellipse2D.Double(0, 0, .5*SIZE, SIZE)
    );
    public static final double FRICTION = 8.0;
    public static final double ANGULAR_DAMPING = 5.0;
    public static final double RESTITUTION = 0.1;
    private static final EntityDisplayConfig DISPLAY = new EntityDisplayConfig()
            .fillColor(Color.YELLOW);

    public Banana(EntityConfig entityConfig, EntityState initState,
                  EventQueue<EngineEvent> eventQueue) {
        super(
            entityConfig.display(DISPLAY),
            initState,
            eventQueue
        );
    }

}
