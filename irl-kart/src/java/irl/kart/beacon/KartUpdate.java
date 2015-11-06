package irl.kart.beacon;

import irl.fw.engine.geometry.Vector2D;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/1/15
 */
public class KartUpdate {

    private final String externalId;
    private final Vector2D center;
    private final Vector2D velocity;

    public KartUpdate(String externalId, Vector2D center, Vector2D velocity) {
        this.externalId = externalId;
        this.center = center;
        this.velocity = velocity;
    }

    public String getExternalId() {
        return externalId;
    }

    public Vector2D getCenter() {
        return center;
    }

    public Vector2D getVelocity() {
        return velocity;
    }

    @Override
    public String toString() {
        return "Beacon update for kart: " + getExternalId();
    }
}
