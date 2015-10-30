package irl.util.universe;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 10/29/15
 */
class UniverseElement<T> {

    private final String id;
    private final T value;

    UniverseElement(String id, T value) {
        this.id = id;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public T getValue() {
        return value;
    }

}
