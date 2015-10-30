package irl.fw.physics.world;

import irl.fw.physics.bodies.Body;
import rx.Observable;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 10/29/15
 */
public class BodyInstance {

    private final Body body;
    private Observable<PhysicalState> stateObservable; //TODO use this
    //TODO add an observer for collisions

    BodyInstance(Body body, Observable<PhysicalState> stateObservable) {
        this.body = body;
        this.stateObservable = stateObservable;
    }

    public Body getBody() {
        return body;
    }

}
