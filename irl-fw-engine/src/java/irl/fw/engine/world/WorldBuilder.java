package irl.fw.engine.world;

import irl.fw.engine.collisions.CollisionResolver;
import irl.fw.engine.collisions.NoopCollisionResolver;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/1/15
 */
public class WorldBuilder {

    private CollisionResolver collisionResolver;

    public WorldBuilder() {
        //set defaults
        collisions(new NoopCollisionResolver());
    }

    public WorldBuilder collisions(CollisionResolver collisionResolver) {
        this.collisionResolver = collisionResolver;
        return this;
    }

    public World build() {
        return new World(collisionResolver);
    }

}
