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

    public static final ImmutableShape SHAPE = new ImmutableShape(
        new Ellipse2D.Double(0, 0, 10, 10)
    );

}
