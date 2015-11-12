package irl.fw.engine.entity;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/11/15
 */
public class EntityId implements Comparable<EntityId> {

    private final String id;

    public EntityId(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        this.id = id;
    }

    private String getId() {
        return id;
    }

    @Override
    public int compareTo(EntityId o) {
        return getId().compareTo(o.getId());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        else if (obj == this) return true;
        else if (!(obj instanceof EntityId)) return false;
        else {
            EntityId other = (EntityId) obj;
            return getId().equals(other.getId());
        }
    }

    @Override
    public String toString() {
        return getId();
    }

}
