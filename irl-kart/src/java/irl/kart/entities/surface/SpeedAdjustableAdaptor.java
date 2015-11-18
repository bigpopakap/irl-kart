package irl.kart.entities.surface;

import irl.fw.engine.entity.EntityId;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/18/15
 */
public class SpeedAdjustableAdaptor {

    private Map<EntityId, Double> allSlowPatches;

    public SpeedAdjustableAdaptor() {
        allSlowPatches = new HashMap<>();
    }

    public synchronized void add(EntityId slowPatchId, double factor) {
        //doesn't matter if this overwrites the value from the same slow patch
        allSlowPatches.put(slowPatchId, factor);
    }

    public synchronized void remove(EntityId slowPatchId) {
        allSlowPatches.remove(slowPatchId);
    }

    public synchronized double getAccumulatedFactor() {
        //get the product of all factors
        return allSlowPatches.values().parallelStream()
                    .reduce(1.0, (a, b) -> a*b);
    }

}
