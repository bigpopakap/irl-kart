package irl.fw.engine.geometry;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/6/15
 */
public class Angle {

    public static final Angle ZERO = Angle.rad(0);
    public static final Angle HALF = Angle.deg(180);
    public static final Angle FULL = Angle.deg(360);

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

    public static Angle random() {
        return deg(Math.random() * FULL.asDeg());
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

    public Angle add(Angle other) {
        if (type == Type.RADIANS && other.type == Type.RADIANS) {
            return Angle.rad(asRad() + other.asRad());
        } else {
            return Angle.deg(asDeg() + other.asDeg());
        }
    }

    public Angle sub(Angle other) {
        if (type == Type.RADIANS && other.type == Type.RADIANS) {
            return Angle.rad(asRad() - other.asRad());
        } else {
            return Angle.deg(asDeg() - other.asDeg());
        }
    }

}
