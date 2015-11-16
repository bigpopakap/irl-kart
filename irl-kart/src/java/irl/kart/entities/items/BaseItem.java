package irl.kart.entities.items;

import irl.util.callbacks.Callback;
import irl.util.callbacks.Callbacks;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/15/15
 */
public abstract class BaseItem implements Item {

    protected final Callbacks onUsed = new Callbacks();
    protected final Callbacks onRemoved = new Callbacks();

    @Override
    public void onUsed(Callback onUsed) {
        this.onUsed.add(onUsed);
    }

    @Override
    public void onRemoved(Callback onRemoved) {
        this.onRemoved.add(onRemoved);
    }

}
