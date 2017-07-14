package irl.kart.entities.weapons;

import irl.fw.engine.entity.EntityId;
import irl.fw.engine.entity.factory.EntityConfig;
import irl.fw.engine.entity.factory.EntityDisplayConfig;
import irl.fw.engine.entity.state.EntityState;
import irl.fw.engine.events.EngineEvent;
import irl.fw.engine.geometry.Angle;
import irl.fw.engine.geometry.ImmutableShape;
import irl.util.reactiveio.EventQueue;

import java.awt.*;
import java.awt.geom.Ellipse2D;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/10/15
 */
public class Shell extends WeaponEntity {

    public static final double SIZE = 10;
    //FIXME this doesn't seem to actually go much faster than a kart
    //FIXME this should also be calculated on-the-fly based on the Kart's speed
    public static final double SPEED = 400.0;
    public static final Angle HELD_ROTATIONAL_SPEED = Angle.rad(2 * Math.PI);
    public static final Angle ROTATIONAL_SPEED = Angle.rad(4 * HELD_ROTATIONAL_SPEED.asRad());
    public static final ImmutableShape SHAPE = new ImmutableShape(
        ImmutableShape.Type.ELLIPSE,
        new Ellipse2D.Double(0, 0, SIZE, .9*SIZE)
    );
    public static final double FRICTION = 0;
    private static final EntityDisplayConfig DISPLAY = new EntityDisplayConfig()
            .fillColor(Color.GREEN);

    private final EntityId sourceKartId;

    public Shell(EntityConfig entityConfig, EntityState initState,
                 EntityId sourceKartId,
                 EventQueue<EngineEvent> eventQueue) {
        super(
            entityConfig.display(DISPLAY),
            initState,
            eventQueue
        );
        this.sourceKartId = sourceKartId;
    }

    public EntityId getSourceKartId() {
        return sourceKartId;
    }

}
