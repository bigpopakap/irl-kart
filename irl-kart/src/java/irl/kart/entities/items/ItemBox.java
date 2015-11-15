package irl.kart.entities.items;

import irl.fw.engine.entity.VirtualEntity;
import irl.fw.engine.entity.actions.remove.EntityRemover;
import irl.fw.engine.entity.actions.remove.RemovableEntity;
import irl.fw.engine.entity.factory.EntityConfig;
import irl.fw.engine.entity.state.EntityState;
import irl.fw.engine.events.EngineEvent;
import irl.fw.engine.geometry.Angle;
import irl.fw.engine.geometry.ImmutableShape;
import irl.util.callbacks.Callback;
import irl.util.reactiveio.Pipe;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/11/15
 */
public class ItemBox extends VirtualEntity implements RemovableEntity {

    public static final Angle INIT_ROT = Angle.deg(45);
    public static final Angle ROTATION_SPEED = Angle.rad(-Math.PI);
    public static final ImmutableShape SHAPE = new ImmutableShape(
        ImmutableShape.Type.RECTANGLE,
        new Rectangle2D.Double(0, 0, 15, 15)
    );

    private final ArrayList<Item> availableItems;
    private final Pipe<EngineEvent> eventQueue;
    private final EntityRemover remover;

    public ItemBox(EntityConfig entityConfig, EntityState initState,
                   Pipe<EngineEvent> eventQueue) {
        this(entityConfig, initState, eventQueue, null);
    }

    public ItemBox(EntityConfig entityConfig, EntityState initState,
                   Pipe<EngineEvent> eventQueue, Callback onRemove) {
        super(entityConfig, initState);

        availableItems = new ArrayList<>();
        this.eventQueue = eventQueue;
        availableItems.add(new ShellItem(this.eventQueue));

        this.remover = new EntityRemover(this, this.eventQueue, onRemove);
    }

    public Item getRandomItem() {
        int randIndex = (int) (availableItems.size() * Math.random());
        return availableItems.get(randIndex);
    }

    @Override
    public void remove() {
        remover.remove();
    }

}
