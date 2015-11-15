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
        return getTypeOrFallback(type, this::getEntity1);
    }

    public <T extends Entity> T getTypeOrSecond(Class<T> type) {
        return getTypeOrFallback(type, this::getEntity2);
    }

    private <T extends Entity> T getTypeOrFallback(Class<T> type, Supplier<Entity> fallback) {
        Entity actual1 = getEntity1();
        Entity actual2 = getEntity2();

        Entity toReturn;

        if (!type.isInstance(actual1) && !type.isInstance(actual2)) {
            //neither are instances of that type
            throw new UnsupportedOperationException("Neither entities are of type " + type);
        }
        else if (type.isInstance(actual1) && type.isInstance(actual2)) {
            //both are instances of that type
            toReturn = fallback.get();
        }
        else if (type.isInstance(actual1)) {
            //only the first is of that type
            toReturn = this.getEntity1();
        }
        else if (type.isInstance(actual2)) {
            //only the second is of that type
            toReturn = this.getEntity2();
        } else {
            throw new IllegalStateException("All cases should have been covered and we shouldn't be here");
        }

        if (type.isInstance(toReturn)) {
            return (T) toReturn;
        } else {
            throw new RuntimeException(String.format("Error casting value (%s) to type %s", toReturn, type));
        }
    }

}
