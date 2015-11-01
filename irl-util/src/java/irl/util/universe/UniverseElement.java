package irl.util.universe;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 10/29/15
 */
class UniverseElement<T> {

    private final T value;

    UniverseElement(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

}
