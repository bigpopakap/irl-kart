package irl.fw.physics.world;

import irl.fw.physics.bodies.Body;
import irl.fw.physics.events.AddBody;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/1/15
 */
public class WorldBuilder {

    private World world;

    public WorldBuilder() {
        world = new World();
    }

    public WorldBuilder addBody(Body body) {
        world.addBody(new AddBody(body));
        return this;
    }

    public World build() {
        World builtWorld = world;
        world = null; //throw it away
        return builtWorld;
    }

}
