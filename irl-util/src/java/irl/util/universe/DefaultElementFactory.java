package irl.util.universe;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/11/15
 */
class DefaultElementFactory<T> implements UniverseElementFactory<T> {

    private final T element;
    private boolean hasReturned;

    public DefaultElementFactory(T element) {
        this.element = element;
        this.hasReturned = false;
    }

    @Override
    public T create(String id) {
        if (hasReturned) {
            throw new IllegalStateException("Element has already been returned");
        }

        hasReturned = true;
        return element;
    }

}
