package irl.kart.entities;

import irl.fw.engine.entity.VirtualEntity;
import irl.fw.engine.entity.state.EntityState;
import irl.fw.engine.events.EngineEvent;
import irl.fw.engine.events.RemoveEntity;
import irl.fw.engine.geometry.Angle;
import irl.fw.engine.geometry.ImmutableShape;
import irl.util.reactiveio.Pipe;

import java.awt.geom.Ellipse2D;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/10/15
 */
public class Shell extends VirtualEntity {

    public static final int SIZE = 10;
    //FIXME this doesn't seem to actually go much faster than a kart
    public static final double SPEED = Kart.MAX_SPEED * 2;
    public static final Angle ROTATIONAL_SPEED = Angle.rad(Math.PI / 16);
    public static final ImmutableShape SHAPE = new ImmutableShape(
        ImmutableShape.Type.ELLIPSE,
        new Ellipse2D.Double(0, 0, SIZE, SIZE)
    );

    private final String sourceKartId;
    private final Pipe<EngineEvent> eventQueue;

    public Shell(String engineId, EntityState initState,
                 String sourceKartId,
                 Pipe<EngineEvent> eventQueue) {
        super(engineId, initState);
        this.sourceKartId = sourceKartId;
        this.eventQueue = eventQueue;
    }

    public String getSourceKartId() {
        return sourceKartId;
    }

    public void remove() {
        this.eventQueue.mergeIn(new RemoveEntity(getEngineId()));
    }

}
