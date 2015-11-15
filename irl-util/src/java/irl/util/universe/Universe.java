package irl.util.universe;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 10/29/15
 */
public class Universe<T> {

    private ConcurrentMap<String, T> universe;

    public Universe() {
        this.universe = new ConcurrentHashMap<>();
    }

    public synchronized String add(String id, UniverseElementFactory<? extends T> factory) {
        if (universe.containsKey(id)) {
            throw new RuntimeException("Duplicate id: " + id);
        } else {
            universe.put(id, factory.create(id));
            return id;
        }
    }

    public synchronized String add(String id, T value) {
        return add(id, new DefaultElementFactory<>(value));
    }

    public synchronized String add(UniverseElementFactory<? extends T> factory) {
        return add(generateId(), factory);
    }

    public synchronized String add(T value) {
        return add(new DefaultElementFactory<>(value));
    }

    public synchronized boolean contains(String id) {
        return universe.containsKey(id);
    }

    public Optional<T> get(String id) {
        if (universe.containsKey(id)) {
            return Optional.of(universe.get(id));
        } else {
            return Optional.empty();
        }
    }

    public synchronized T remove(String id) {
        return universe.remove(id);
    }

    public synchronized T update(String id, T newValue) {
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

    public Collection<T> toCollection() {
        return universe.values();
    }

}
