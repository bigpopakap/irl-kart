package irl.kart.events.kart;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/11/15
 */
public class SpinKart extends KartEvent {

    private final String kartId;

    public SpinKart(String kartId) {
        this.kartId = kartId;
    }

    public String getKartId() {
        return kartId;
    }

}
