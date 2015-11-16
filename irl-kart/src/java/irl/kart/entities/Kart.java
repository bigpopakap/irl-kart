package irl.kart.entities;

import irl.fw.engine.entity.factory.EntityConfig;
import irl.fw.engine.entity.joints.JointPoint;
import irl.fw.engine.entity.state.EntityState;
import irl.fw.engine.events.EngineEvent;
import irl.fw.engine.events.UpdateEntity;
import irl.fw.engine.geometry.ImmutableShape;
import irl.kart.beacon.KartBeacon;
import irl.fw.engine.entity.IRLEntity;
import irl.kart.beacon.SingleKartBeacon;
import irl.kart.entities.items.Item;
import irl.kart.entities.items.actions.itemuser.ItemUser;
import irl.kart.entities.items.actions.itemuser.ItemUserAdaptor;
import irl.kart.entities.weapons.Banana;
import irl.kart.entities.weapons.Shell;
import irl.kart.entities.weapons.WeaponEntity;
import irl.kart.entities.weapons.WeaponTarget;
import irl.kart.events.beacon.HoldItem;
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
public class Kart extends IRLEntity implements ItemUser, WeaponTarget {

    //TODO this kart shouldn't know about its length and shape.
    //      that should come from the beacon
    public static final int KART_LENGTH = 20;
    public static final ImmutableShape SHAPE = new ImmutableShape(
        ImmutableShape.Type.CONVEX_POLY,
        new Polygon(
            new int[] { 10, 10, 5,           0,  0},
            new int[] {  0, 15, KART_LENGTH, 15, 0},
            5
        )
    );

    private final String kartId;
    private final SingleKartBeacon kartBeacon;
    private final EventQueue<EngineEvent> eventQueue;
    private final ItemUserAdaptor<Kart> itemUser;

    public Kart(EntityConfig entityConfig, EntityState initState,
                String kartId, KartBeacon kartBeacon,
                EventQueue<EngineEvent> eventQueue) {
        super(entityConfig, initState);

        this.kartId = kartId;
        this.kartBeacon = new SingleKartBeacon(kartId, kartBeacon);
        this.eventQueue = eventQueue;
        this.itemUser = new ItemUserAdaptor<>(this);

        //merge in update position events
        this.eventQueue.mergeIn(
            //TODO we should only report the latest position or something
            this.kartBeacon.stream()
                .ofType(KartStateUpdate.class)
                .filter(update -> StringUtils.equal(getKartId(), update.getKartId()))
                .map(update -> new UpdateEntity(getEngineId(), update.getStateUpdate()))
        );

        //merge in uses of items
        this.kartBeacon.stream()
            .ofType(HoldItem.class)
            .subscribe(update -> this.holdItem());
        this.kartBeacon.stream()
            .ofType(UseItem.class)
            .subscribe(update -> this.useItem());
    }

    public String getKartId() {
        return kartId;
    }

    @Override
    public void hitBy(WeaponEntity weapon) {
        if (weapon instanceof Shell || weapon instanceof Banana) {
            spin();
        } else {
            System.err.println(
                String.format("Kart %s was hit by weapon type %s, but doesn't know how to react to it",
                        getKartId(), weapon.getClass())
            );
        }
    }

    public void spin() {
        kartBeacon.send(new SpinKart(getKartId()));
    }

    @Override
    public void takeItem(Item item) {
        itemUser.takeItem(item);
    }

    @Override
    public void holdItem() {
        itemUser.holdItem();
    }

    @Override
    public void useItem() {
        itemUser.useItem();
    }

    @Override
    public JointPoint getItemHoldPoint() {
        return new JointPoint(this, getState().getCenter());
    }

}
