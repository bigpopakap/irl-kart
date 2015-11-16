package irl.kart.events.kart;

import irl.fw.engine.events.EngineEvent;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/11/15
 */
public abstract class KartEvent implements EngineEvent {

    private final String kartId;

    public KartEvent(String kartId) {
        this.kartId = kartId;
    }

    public String getKartId() {
        return kartId;
    }

}
