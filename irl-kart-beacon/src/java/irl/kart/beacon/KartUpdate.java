package irl.kart.beacon;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/1/15
 */
public class KartUpdate {

    private final String kartId;
    private final String position; //TODO not just a string

    KartUpdate(String kartId, String position) {
        this.kartId = kartId;
        this.position = position;
    }

    public String getKartId() {
        return kartId;
    }

    public String getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return getKartId() + ":" + getPosition();
    }
}
