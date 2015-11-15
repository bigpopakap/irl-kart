package irl.kart.entities;

import irl.fw.engine.entity.VirtualEntity;
import irl.fw.engine.entity.actions.remove.RemovableEntity;
import irl.fw.engine.entity.actions.remove.RemovableEntityAdaptor;
import irl.fw.engine.entity.factory.EntityConfig;
import irl.fw.engine.entity.state.EntityState;
import irl.fw.engine.events.EngineEvent;
import irl.fw.engine.geometry.ImmutableShape;
import irl.util.reactiveio.EventQueue;

import java.awt.*;


/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/15/15
 */
public class Banana extends VirtualEntity implements RemovableEntity {

    public static final int SIZE = 10;
    private static final int WIDTH = 4;
    public static final ImmutableShape SHAPE = new ImmutableShape(
        ImmutableShape.Type.CONVEX_POLY,
        new Polygon(
            new int[] { (SIZE-WIDTH)/2, (SIZE+WIDTH)/2, SIZE,           SIZE,           (SIZE+WIDTH)/2, (SIZE-WIDTH)/2, 0,              0 },
            new int[] { 0,              0,              (SIZE-WIDTH)/2, (SIZE+WIDTH)/2, SIZE,           SIZE,           (SIZE+WIDTH)/2, (SIZE-WIDTH)/2},
            8
        )
    );

    private final RemovableEntityAdaptor remover;

    public Banana(EntityConfig entityConfig, EntityState initState,
                  EventQueue<EngineEvent> eventQueue) {
        super(entityConfig, initState);
        this.remover = new RemovableEntityAdaptor(this, eventQueue);
    }

    @Override
    public void remove() {
        remover.remove();
    }

}
