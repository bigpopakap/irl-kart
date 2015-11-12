package irl.kart.beacon;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/11/15
 */
public abstract class KartBeaconEvent {

    private final String kartId;

    public KartBeaconEvent(String kartId) {
        this.kartId = kartId;
    }

    public String getKartId() {
        return kartId;
    }

    @Override
    public String toString() {
        return "Beacon updateEntity for kart: " + getKartId();
    }

}
