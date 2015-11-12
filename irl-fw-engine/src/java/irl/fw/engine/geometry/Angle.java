package irl.fw.engine.geometry;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/6/15
 */
public class Angle {

    public static final Angle ZERO = Angle.rad(0);

    private enum Type {
        RADIANS, DEGREES;
    }

    private final double value;
    private final Type type;

    private Angle(double value, Type type) {
        this.value = value;
        this.type = type;
    }

    public static Angle deg(double value) {
        return new Angle(value, Type.DEGREES);
    }

    public static Angle rad(double value) {
        return new Angle(value, Type.RADIANS);
    }

    public double asDeg() {
        switch (type) {
            case DEGREES: return value;
            case RADIANS: return Math.toRadians(value);
            default: throw new IllegalStateException("Unexpected angle type: " + type);
        }
    }

    public double asRad() {
        switch (type) {
            case DEGREES: return Math.toRadians(value);
            case RADIANS: return value;
            default: throw new IllegalStateException("Unexpected angle type: " + type);
        }
    }

}
