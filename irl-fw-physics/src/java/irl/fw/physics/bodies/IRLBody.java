package irl.fw.physics.bodies;

import irl.fw.physics.events.UpdateBody;
import rx.Observable;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 10/29/15
 */
public abstract class IRLBody implements Body {

    public abstract Observable<UpdateBody> getUpdates();

    @Override
    public boolean isVirtual() {
        return false;
    }

}
