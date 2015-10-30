package irl.fw.physics.bodies;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 10/29/15
 */
public interface IRLBody extends Body {

    @Override
    default boolean isVirtual() {
        return false;
    }

}
