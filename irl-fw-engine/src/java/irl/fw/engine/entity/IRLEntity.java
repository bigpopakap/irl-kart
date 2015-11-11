package irl.fw.engine.entity;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 10/29/15
 */
public abstract class IRLEntity implements Entity {

    @Override
    public final boolean isVirtual() {
        return false;
    }
}
