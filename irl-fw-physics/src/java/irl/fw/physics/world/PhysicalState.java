package irl.fw.physics.world;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 10/29/15
 */
public class PhysicalState {

    private final String value;
    //TODO rotation, position, vel, accel

    public PhysicalState(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return getValue();
    }
}
