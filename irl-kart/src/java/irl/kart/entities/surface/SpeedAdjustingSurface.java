package irl.kart.entities.surface;

import irl.fw.engine.entity.Entity;
import irl.fw.engine.entity.EntityType;
import irl.fw.engine.entity.VirtualEntity;
import irl.fw.engine.entity.factory.EntityConfig;
import irl.fw.engine.entity.state.EntityState;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/18/15
 */
public abstract class SpeedAdjustingSurface extends VirtualEntity {

    public SpeedAdjustingSurface(EntityConfig entityConfig, EntityState initState) {
        super(entityConfig, initState);
    }

    protected abstract double slowFactor();

    @Override
    public EntityType getEntityType() {
        return EntityType.FIXED;
    }

    @Override
    public boolean collide(Entity other) {
        if (other instanceof SpeedAdjustable) {
            SpeedAdjustable speedAdjustable = (SpeedAdjustable) other;
            speedAdjustable.slowBy(getEngineId(), slowFactor());
        }

        //this doesn't interact with any other object
        //FIXME by returning false, the afterCollide() method doesn't get called
        return false;
    }

    @Override
    public void afterCollide(Entity other) {
        if (other instanceof SpeedAdjustable) {
            SpeedAdjustable speedAdjustable = (SpeedAdjustable) other;
            speedAdjustable.unslow(getEngineId());
        }
    }

}
