package irl.util.universe;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 10/29/15
 */
public class Universe<T> {

    private ConcurrentMap<String, UniverseElement<T>> universe;

    public Universe() {
        this.universe = new ConcurrentHashMap<>();
    }

    public String add(T value) {
        String id = generateId();
        UniverseElement<T> element = new UniverseElement<>(id, value);
        universe.put(id, element);
        return id;
    }

    public boolean contains(String id) {
        return universe.containsKey(id);
    }

    public T get(String id) {
        return unpack(universe.get(id));
    }

    public T remove(String id) {
        return unpack(universe.remove(id));
    }

    public T update(String id, T newValue) {
        return unpack(universe.replace(id, new UniverseElement<T>(id, newValue)));
    }

    private T unpack(UniverseElement<T> element) {
        return element != null ? element.getValue() : null;
    }

    private String generateId() {
        String id;
        do {
            id = String.valueOf(universe.size());
        }
        while (universe.containsKey(id));

        return id;
    }

}
