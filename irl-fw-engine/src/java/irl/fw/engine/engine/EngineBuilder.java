package irl.fw.engine.engine;

import irl.fw.engine.collisions.CollisionResolver;
import irl.fw.engine.collisions.impl.NoopCollisionResolver;
import irl.fw.engine.graphics.Renderer;
import irl.fw.engine.graphics.impl.NoopRenderer;
import irl.fw.engine.physics.PhysicsModeler;
import irl.fw.engine.physics.impl.dyn4j.Dyn4jPhysicsModeler;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/1/15
 */
public class EngineBuilder {

    private PhysicsModeler physicsModel;
    private CollisionResolver collisionResolver;
    private Renderer renderer;

    public EngineBuilder() {
        //set defaults
        physics(new Dyn4jPhysicsModeler());
        collisions(new NoopCollisionResolver());
        renderer(new NoopRenderer());
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
        return new Engine(physicsModel, collisionResolver, renderer);
    }

}
