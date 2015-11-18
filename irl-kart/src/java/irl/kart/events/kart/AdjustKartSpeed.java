package irl.kart.events.kart;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/18/15
 */
public class AdjustKartSpeed extends KartEvent {

    private final double factor;

    public AdjustKartSpeed(String kartId, double factor) {
        super(kartId);
        this.factor = factor;
    }

    public double getFactor() {
        return factor;
    }

}
