package irl.util.universe;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 10/29/15
 */
public class Universe<T> implements Iterable<Map.Entry<String, T>> {

    private ConcurrentMap<String, T> universe;

    public Universe() {
        this.universe = new ConcurrentHashMap<>();
    }

    public String add(T value) {
        String id = generateId();
        universe.put(id, value);
        return id;
    }

    public boolean contains(String id) {
        return universe.containsKey(id);
    }

    public T get(String id) {
        return universe.get(id);
    }

    public T remove(String id) {
        return universe.remove(id);
    }

    public T update(String id, T newValue) {
        return universe.replace(id, newValue);
    }

    private String generateId() {
        String id;
        do {
            id = String.valueOf(universe.size());
        }
        while (universe.containsKey(id));

        return id;
    }

    @Override
    public Iterator<Map.Entry<String, T>> iterator() {
        return universe.entrySet().iterator();
    }
}
