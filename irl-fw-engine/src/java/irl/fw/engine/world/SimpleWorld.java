package irl.fw.engine.world;

import irl.fw.engine.entity.EntityInstance;

import java.util.Collection;
import java.util.Collections;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/6/15
 */
public class SimpleWorld implements World {

    private final Collection<EntityInstance> entities;

    public SimpleWorld(Collection<EntityInstance> entities) {
        this.entities = Collections.unmodifiableCollection(entities);
    }

    @Override
    public Collection<EntityInstance> getEntities() {
        return entities;
    }

}
