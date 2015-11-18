package irl.kart.entities.surface;

import irl.fw.engine.entity.EntityId;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/18/15
 */
public interface SpeedAdjustable {

    void slowBy(EntityId slowPatchId, double factor);
    void unslow(EntityId slowPatchId);

}
