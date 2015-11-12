package irl.kart.entities;

import irl.fw.engine.entity.VirtualEntity;
import irl.fw.engine.geometry.ImmutableShape;

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
    public static final ImmutableShape SHAPE = new ImmutableShape(
        ImmutableShape.Type.ELLIPSE,
        new Ellipse2D.Double(0, 0, SIZE, SIZE)
    );

    private final String sourceKartId;

    public Shell(String sourceKartId) {
        this.sourceKartId = sourceKartId;
    }

    public String getSourceKartId() {
        return sourceKartId;
    }

}
