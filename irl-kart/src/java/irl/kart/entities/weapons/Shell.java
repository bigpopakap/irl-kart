package irl.kart.entities.weapons;

import irl.fw.engine.entity.EntityId;
import irl.fw.engine.entity.factory.EntityConfig;
import irl.fw.engine.entity.state.EntityState;
import irl.fw.engine.events.EngineEvent;
import irl.fw.engine.geometry.Angle;
import irl.fw.engine.geometry.ImmutableShape;
import irl.util.reactiveio.EventQueue;

import java.awt.geom.Ellipse2D;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/10/15
 */
public class Shell extends WeaponEntity {

    public static final int SIZE = 15;
    //FIXME this doesn't seem to actually go much faster than a kart
    //FIXME this should also be calculated on-the-fly based on the Kart's speed
    public static final double SPEED = 300;
    public static final Angle ROTATIONAL_SPEED = Angle.rad(8 * Math.PI);
    public static final ImmutableShape SHAPE = new ImmutableShape(
        ImmutableShape.Type.ELLIPSE,
        new Ellipse2D.Double(0, 0, SIZE, 7*SIZE/8)
    );

    private final EntityId sourceKartId;

    public Shell(EntityConfig entityConfig, EntityState initState,
                 EntityId sourceKartId,
                 EventQueue<EngineEvent> eventQueue) {
        super(entityConfig, initState, eventQueue);
        this.sourceKartId = sourceKartId;
    }

    public EntityId getSourceKartId() {
        return sourceKartId;
    }

}
