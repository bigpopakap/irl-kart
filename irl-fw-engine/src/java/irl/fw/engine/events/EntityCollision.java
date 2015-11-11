package irl.fw.engine.events;

import irl.fw.engine.entity.Entity;
import irl.fw.engine.entity.EntityInstance;

import java.util.function.Supplier;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 10/30/15
 */
public class EntityCollision implements EngineEvent {

    private final EntityInstance entity1;
    private final EntityInstance entity2;

    public EntityCollision(EntityInstance entity1, EntityInstance entity2) {
        this.entity1 = entity1;
        this.entity2 = entity2;
    }

    public EntityInstance getEntity1() {
        return entity1;
    }

    public EntityInstance getEntity2() {
        return entity2;
    }

    public boolean isBetween(Class<? extends Entity> type1, Class<? extends Entity> type2) {
        Entity actual1 = getEntity1().getEntity();
        Entity actual2 = getEntity2().getEntity();

        return (type1.isInstance(actual1) && type2.isInstance(actual2))
            || (type1.isInstance(actual2) && type2.isInstance(actual1));
    }

    public EntityInstance getType(Class<? extends Entity> type) {
        return getTypeOrFallback(type, () -> {
            throw new UnsupportedOperationException(
                "Unclear which to return. Both entities are of type " + type
            );
        });
    }

    public EntityInstance getTypeOrFirst(Class<? extends Entity> type) {
        return getTypeOrFallback(type, () -> this.getEntity1());
    }

    public EntityInstance getTypeOrSecond(Class<? extends Entity> type) {
        return getTypeOrFallback(type, () -> this.getEntity2());
    }

    private EntityInstance getTypeOrFallback(Class<? extends Entity> type, Supplier<EntityInstance> fallback) {
        EntityInstance actual1 = getEntity1();
        EntityInstance actual2 = getEntity2();

        if (!type.isInstance(actual1.getEntity()) && !type.isInstance(actual2.getEntity())) {
            //neither are instances of that type
            throw new UnsupportedOperationException("Neither entities are of type " + type);
        }
        else if (type.isInstance(actual1.getEntity()) && type.isInstance(actual2.getEntity())) {
            //both are instances of that type
            return fallback.get();
        }
        else if (type.isInstance(actual1.getEntity())) {
            //only the first is of that type
            return getEntity1();
        }
        else if (type.isInstance(actual2.getEntity())) {
            //only the second is of that type
            return getEntity2();
        } else {
            throw new IllegalStateException("All cases should have been covered and we shouldn't be here");
        }
    }

}
