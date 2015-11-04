package irl.fw.engine.engine;

import irl.fw.engine.collisions.CollisionResolver;
import irl.fw.engine.collisions.impl.NoopCollisionResolver;
import irl.fw.engine.graphics.Renderer;
import irl.fw.engine.graphics.impl.NoopRenderer;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/1/15
 */
public class EngineBuilder {

    private CollisionResolver collisionResolver;
    private Renderer renderer;

    public EngineBuilder() {
        //set defaults
        collisions(new NoopCollisionResolver());
        renderer(new NoopRenderer());
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
        return new Engine(collisionResolver, renderer);
    }

}
