package irl.fw.engine.events;

import irl.fw.engine.entity.Entity;

import java.util.function.Supplier;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 10/30/15
 */
public class EntityCollision implements EngineEvent {

    private final Entity entity1;
    private final Entity entity2;

    public EntityCollision(Entity entity1, Entity entity2) {
        this.entity1 = entity1;
        this.entity2 = entity2;
    }

    public Entity getEntity1() {
        return entity1;
    }

    public Entity getEntity2() {
        return entity2;
    }

    public boolean isWith(Class<? extends Entity> type) {
        Entity actual1 = getEntity1();
        Entity actual2 = getEntity2();

        return type.isInstance(actual1) || type.isInstance(actual2);
    }

    public boolean isWith(Class<? extends Entity> type1, Class<? extends Entity> type2) {
        Entity actual1 = getEntity1();
        Entity actual2 = getEntity2();

        return (type1.isInstance(actual1) && type2.isInstance(actual2))
            || (type1.isInstance(actual2) && type2.isInstance(actual1));
    }

    public <T extends Entity> T getType(Class<T> type) {
        return getTypeOrFallback(type, () -> {
            throw new UnsupportedOperationException(
                "Unclear which to return. Both entities are of type " + type
            );
        });
    }

    public <T extends Entity> T getTypeOrFirst(Class<T> type) {
        return getTypeOrFallback(type, () -> {
            @SuppressWarnings("unchecked")
            T toReturn = (T) this.getEntity1();
            return toReturn;
        });
    }

    public <T extends Entity> T getTypeOrSecond(Class<T> type) {
        return getTypeOrFallback(type, () -> {
            @SuppressWarnings("unchecked")
            T toReturn = (T) this.getEntity2();
            return toReturn;
        });
    }

    private <T extends Entity> T getTypeOrFallback(Class<T> type, Supplier<T> fallback) {
        Entity actual1 = getEntity1();
        Entity actual2 = getEntity2();

        if (!type.isInstance(actual1) && !type.isInstance(actual2)) {
            //neither are instances of that type
            throw new UnsupportedOperationException("Neither entities are of type " + type);
        }
        else if (type.isInstance(actual1) && type.isInstance(actual2)) {
            //both are instances of that type
            return fallback.get();
        }
        else if (type.isInstance(actual1)) {
            //only the first is of that type
            @SuppressWarnings("unchecked")
            T toReturn = (T) this.getEntity1();
            return toReturn;
        }
        else if (type.isInstance(actual2)) {
            //only the second is of that type
            @SuppressWarnings("unchecked")
            T toReturn = (T) this.getEntity2();
            return toReturn;
        } else {
            throw new IllegalStateException("All cases should have been covered and we shouldn't be here");
        }
    }

}
