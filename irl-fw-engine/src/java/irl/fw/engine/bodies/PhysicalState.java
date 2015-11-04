package irl.fw.engine.bodies;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 10/29/15
 */
public class PhysicalState {

    private final int value;
    //TODO rotation, position, vel, accel

    public PhysicalState(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(getValue());
    }
}
