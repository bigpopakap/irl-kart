package irl.fw.engine.entity;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/1/15
 */
public class EntityInstance {

    private final Entity entity;
    private final EntityState state;

    public EntityInstance(Entity entity, EntityState state) {
        this.entity = entity;
        this.state = state;
    }

    public Entity getEntity() {
        return entity;
    }

    public EntityState getState() {
        return state;
    }

    public EntityInstance setState(EntityState state) {
        return new EntityInstance(entity, state);
    }

}
