package irl.fw.engine.graphics.impl;

import irl.fw.engine.graphics.Renderer;
import irl.fw.engine.entity.EntityInstance;

import java.util.Collection;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/3/15
 */
public class NoopRenderer implements Renderer {

    @Override
    public void render(Collection<EntityInstance> entities, long timeStep) {
        //do nothing
    }

}
