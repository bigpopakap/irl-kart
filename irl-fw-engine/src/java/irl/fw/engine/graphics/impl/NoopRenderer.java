package irl.fw.engine.graphics.impl;

import irl.fw.engine.graphics.Renderer;
import irl.fw.engine.bodies.BodyInstance;

import java.util.Collection;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/3/15
 */
public class NoopRenderer implements Renderer {

    @Override
    public void render(Collection<BodyInstance> bodies, long timeStep) {
        //do nothing
    }

}
