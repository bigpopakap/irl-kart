package irl.kart.entities;

import irl.fw.engine.entity.factory.EntityConfig;
import irl.fw.engine.entity.state.EntityState;
import irl.fw.engine.events.EngineEvent;
import irl.fw.engine.events.UpdateEntity;
import irl.fw.engine.geometry.ImmutableShape;
import irl.kart.beacon.KartBeacon;
import irl.fw.engine.entity.IRLEntity;
import irl.kart.entities.items.Item;
import irl.kart.entities.items.actions.itemuser.ItemUser;
import irl.kart.entities.items.actions.itemuser.ItemUserAdaptor;
import irl.kart.events.beacon.KartStateUpdate;
import irl.kart.events.beacon.UseItem;
import irl.kart.events.kart.SpinKart;
import irl.util.reactiveio.EventQueue;
import irl.util.string.StringUtils;

import java.awt.*;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/1/15
 */
public class Kart extends IRLEntity implements ItemUser {

    //TODO this kart shouldn't know about its length and shape.
    //      that should come from the beacon
    public static final int KART_LENGTH = 20;
    public static final ImmutableShape SHAPE = new ImmutableShape(
        ImmutableShape.Type.CONVEX_POLYGON,
        new Polygon(
            new int[] { 10, 10,  5,  0, 0},
            new int[] {  0, 15, KART_LENGTH, 15, 0},
            5
        )
    );

    private final String kartId;
    private final KartBeacon kartBeacon;
    private final EventQueue<EngineEvent> eventQueue;
    private final ItemUserAdaptor<Kart> itemUser;

    public Kart(EntityConfig entityConfig, EntityState initState,
                String kartId, KartBeacon kartBeacon,
                EventQueue<EngineEvent> eventQueue) {
        super(entityConfig, initState);

        this.kartId = kartId;
        this.kartBeacon = kartBeacon;
        this.eventQueue = eventQueue;
        this.itemUser = new ItemUserAdaptor<>(this);

        //merge in update position events
        this.eventQueue.mergeIn(
            //TODO we should only report the latest position or something
            kartBeacon.stream()
                .ofType(KartStateUpdate.class)
                .filter(update -> StringUtils.equal(getKartId(), update.getKartId()))
                .map(update -> new UpdateEntity(getEngineId(), update.getStateUpdate()))
        );

        //merge in uses of items
        kartBeacon.stream()
            .ofType(UseItem.class)
            .filter(update -> StringUtils.equal(getKartId(), update.getKartId()))
            .subscribe(update -> this.useItem());
    }

    public String getKartId() {
        return kartId;
    }

    public void spin() {
        kartBeacon.send(new SpinKart(getKartId()));
    }

    @Override
    public void takeItem(Item item) {
        itemUser.takeItem(item);
    }

    @Override
    public void useItem() {
        itemUser.useItem();
    }

}
