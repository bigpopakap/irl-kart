package irl.fw.engine.entity;

import irl.fw.engine.collisions.CollidableEntity;
import irl.fw.engine.entity.factory.EntityConfig;
import irl.fw.engine.entity.factory.EntityDisplayConfig;
import irl.fw.engine.entity.state.EntityState;
import irl.fw.engine.entity.state.EntityStateUpdate;
import irl.util.reactiveio.EventQueue;
import irl.util.serialization.JSONSerializable;
import rx.Observable;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 10/29/15
 */
public abstract class Entity implements EngineElement, CollidableEntity, JSONSerializable {

    private final EntityId engineId;
    private final EntityDisplayConfig displayConfig;
    private volatile EntityState state;

    private EventQueue<EntityState> states;

    public Entity(EntityConfig entityConfig, EntityState initState) {
        if (entityConfig == null || entityConfig.getId() == null
            || entityConfig.getDisplayConfig() == null) {
            throw new IllegalArgumentException("These cannot be null");
        }

        this.engineId = entityConfig.getId();
        this.displayConfig = entityConfig.getDisplayConfig();
        this.states = new EventQueue<>();
        setState(initState);
    }

    public abstract boolean isVirtual();

    @Override
    public EntityId getEngineId() {
        return engineId;
    }

    public EntityDisplayConfig getDisplayConfig() {
        return displayConfig;
    }

    public EntityState getState() {
        return state;
    }

    public Observable<EntityState> getStates() {
        return states.get();
    }

    public synchronized void setState(EntityState state) {
        this.state = state;
        this.states.mergeIn(this.state);
    }

    public synchronized void updateState(EntityStateUpdate stateUpdates) {
        setState(stateUpdates.fillAndBuild(getState()));
    }

    @Override
    public boolean collide(Entity other) {
        return true;
    }

    @Override
    public String toJSON() {
        // TODO use Gson
        return String.format(
            "{ " +
                "\"id\": \"%s\", " +
                "\"entityClass\": \"%s\", " +
                "\"width\": %s, " +
                "\"height\": %s, " +
                "\"centerX\": %s, " +
                "\"centerY\": %s, " +
                "\"rotationRads\": %s " +
            "}",
            getEngineId(),
            getClass(), // TODO define this explicitly instead of using the class
            getState().getShape().getBounds2D().getWidth(),
            getState().getShape().getBounds2D().getHeight(),
            getState().getCenter().getX(),
            getState().getCenter().getY(),
            getState().getRotation().asRad()
        );
    }
}
