package irl.fw.physics.world;

import irl.fw.physics.bodies.Body;
import irl.fw.physics.bodies.IRLBody;
import irl.fw.physics.bodies.VirtualBody;
import irl.util.universe.Universe;
import rx.Observable;

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
    public String addIRLBody(IRLBody body, Observable<PhysicalState> stateObservable) {
        return universe.add(new BodyInstance(body, stateObservable));
    }

    public String addVirtualBody(VirtualBody body) {
        return universe.add(new BodyInstance(body, null)); //TODO add an observable for the state
    }

}