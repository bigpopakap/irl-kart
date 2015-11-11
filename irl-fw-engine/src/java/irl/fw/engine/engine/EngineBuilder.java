package irl.fw.engine.engine;

import irl.fw.engine.collisions.CollisionResolver;
import irl.fw.engine.collisions.impl.NoopCollisionResolver;
import irl.fw.engine.events.EngineEvent;
import irl.fw.engine.graphics.Renderer;
import irl.fw.engine.graphics.impl.NoopRenderer;
import irl.fw.engine.physics.PhysicsModeler;
import irl.fw.engine.physics.impl.dyn4j.Dyn4jPhysicsModeler;
import rx.Observable;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/1/15
 */
public class EngineBuilder {

    private Observable<? extends EngineEvent> extraEvents;
    private PhysicsModeler physicsModel;
    private CollisionResolver collisionResolver;
    private Renderer renderer;

    public EngineBuilder() {
        //set defaults
        extraEvents = Observable.empty();
        physics(new Dyn4jPhysicsModeler());
        collisions(new NoopCollisionResolver());
        renderer(new NoopRenderer());
    }

    public EngineBuilder extraEvents(Observable<? extends EngineEvent> extraEvents) {
        this.extraEvents = extraEvents;
        return this;
    }

    public EngineBuilder physics(PhysicsModeler physicsModel) {
        this.physicsModel = physicsModel;
        return this;
    }

    public EngineBuilder collisions(CollisionResolver collisionResolver) {
        this.collisionResolver = collisionResolver;
        return this;
    }

    public EngineBuilder renderer(Renderer renderer) {
        this.renderer = renderer;
        return this;
    }

    public Engine build() {
        return new Engine(extraEvents,
                        physicsModel, collisionResolver,
                        renderer);
    }

}
