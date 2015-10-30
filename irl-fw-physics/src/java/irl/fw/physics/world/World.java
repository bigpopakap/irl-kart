package irl.fw.physics.world;

import irl.fw.physics.bodies.Body;
import irl.util.universe.Universe;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 10/29/15
 */
public class World {

    private final Universe<BodyInstance> universe;

    public World() {
        universe = new Universe<>();
    }

    //TODO how to set initial position and stuff?
    public String addBody(Body body) {
        return universe.add(new BodyInstance(body));
    }

}
