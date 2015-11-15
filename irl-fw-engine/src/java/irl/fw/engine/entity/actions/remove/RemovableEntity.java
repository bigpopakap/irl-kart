package irl.fw.engine.entity.actions.remove;

import irl.util.callbacks.Callback;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/12/15
 */
public interface RemovableEntity {

    void remove();
    String onRemove(Callback callback);

}
