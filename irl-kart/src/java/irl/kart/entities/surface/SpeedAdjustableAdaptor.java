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

    public synchronized boolean add(EntityId slowPatchId, double factor) {
        //doesn't matter if this overwrites the value from the same slow patch
        boolean alreadyExisted = allSlowPatches.containsKey(slowPatchId);
        allSlowPatches.put(slowPatchId, factor);
        return !alreadyExisted;
    }

    public synchronized boolean remove(EntityId slowPatchId) {
        boolean alreadyExisted = allSlowPatches.containsKey(slowPatchId);
        allSlowPatches.remove(slowPatchId);
        return alreadyExisted;
    }

    public synchronized double getAccumulatedFactor() {
        //get the product of all factors
        return allSlowPatches.values().parallelStream()
                    .reduce(1.0, (a, b) -> a*b);
    }

}
