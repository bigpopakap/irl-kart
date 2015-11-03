package irl.fw.graphics;

import rx.Observable;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/2/15
 */
public interface Renderable {

    Observable<Frame> frames();

}
