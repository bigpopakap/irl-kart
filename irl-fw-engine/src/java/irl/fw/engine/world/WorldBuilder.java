package irl.fw.engine.world;

import irl.fw.engine.collisions.CollisionResolver;
import irl.fw.engine.collisions.NoopCollisionResolver;
import irl.fw.physics.modeling.NoopPhysicsModeler;
import irl.fw.physics.modeling.PhysicsModeler;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/1/15
 */
public class WorldBuilder {

    private PhysicsModeler physicsModeler;
    private CollisionResolver collisionResolver;

    public WorldBuilder() {
        //set defaults
        collisions(new NoopCollisionResolver());
        physics(new NoopPhysicsModeler());
    }

    public WorldBuilder physics(PhysicsModeler physicsModeler) {
        this.physicsModeler = physicsModeler;
        return this;
    }

    public WorldBuilder collisions(CollisionResolver collisionResolver) {
        this.collisionResolver = collisionResolver;
        return this;
    }

    public World build() {
        return new World(physicsModeler, collisionResolver);
    }

}
