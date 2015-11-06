package irl.fw.engine.physics.impl.dyn4j;

import irl.fw.engine.geometry.Angle;
import irl.fw.engine.geometry.Vector2D;
import org.dyn4j.geometry.Vector2;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/6/15
 */
class Dyn4jConverter {

    public static Vector2 fromVector(Vector2D vector) {
        return new Vector2(vector.getX(), vector.getY());
    }

    public static Vector2D toVector(Vector2 vector) {
        return new Vector2D(vector.x, vector.y);
    }

    public static Angle toRadAngle(double rads) {
        return Angle.rad(rads);
    }

}
