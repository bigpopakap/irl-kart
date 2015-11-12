package irl.kart.entities;

import irl.fw.engine.entity.state.EntityState;
import irl.fw.engine.entity.state.EntityStateBuilder;
import irl.fw.engine.events.AddEntity;
import irl.fw.engine.events.EngineEvent;
import irl.fw.engine.events.UpdateEntity;
import irl.fw.engine.geometry.ImmutableShape;
import irl.fw.engine.geometry.Vector2D;
import irl.kart.beacon.KartBeacon;
import irl.fw.engine.entity.IRLEntity;
import irl.kart.events.beacon.FireWeapon;
import irl.kart.events.beacon.KartStateUpdate;
import irl.kart.events.kart.SpinKart;
import irl.util.reactiveio.Pipe;
import irl.util.string.StringUtils;

import java.awt.*;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/1/15
 */
public class Kart extends IRLEntity {

    public static final double MAX_SPEED = 120;
    public static final double MIN_SPEED = -60;
    public static final double SPEED_INCR = 20;
    public static final double ROT_INCR = 15;

    public static final int KART_LENGTH = 20;
    public static final ImmutableShape SHAPE = new ImmutableShape(
        ImmutableShape.Type.POLYGON,
        new Polygon(
            new int[] { 10, 10,  5,  0, 0},
            new int[] {  0, 15, KART_LENGTH, 15, 0},
            5
        )
    );

    private final String kartId;
    private final KartBeacon kartBeacon;
    private final Pipe<EngineEvent> eventQueue;

    public Kart(String engineId, EntityState initState,
                String kartId, KartBeacon kartBeacon,
                Pipe<EngineEvent> eventQueue) {
        super(engineId, initState);

        this.kartId = kartId;
        this.kartBeacon = kartBeacon;
        this.eventQueue = eventQueue;

        //merge in update position events
        this.eventQueue.mergeIn(
            //TODO we should only report the latest position or something
            kartBeacon.stream()
                .ofType(KartStateUpdate.class)
                .filter(update -> StringUtils.equal(getKartId(), update.getKartId()))
                .map(update -> new UpdateEntity(getEngineId(), update.getStateUpdate()))
        );

        //merge in fire-weapon events
        this.eventQueue.mergeIn(
            this.kartBeacon.stream()
                    .ofType(FireWeapon.class)
                    .filter(fireWeapon -> StringUtils.equal(getKartId(), fireWeapon.getKartId()))
                    .map(fireWeapon -> this.fire())
                    .filter(addEntity -> addEntity != null)
        );
    }

    public String getKartId() {
        return kartId;
    }

    public void spin() {
        kartBeacon.send(new SpinKart(getKartId()));
    }

    private AddEntity fire() {
        EntityState kartState = getState();
        Vector2D kartCenter = kartState.getCenter();

        Vector2D shellVelocity = new Vector2D(0, Shell.SPEED).rotate(kartState.getRotation());
        Vector2D shellCenter = kartCenter.add(
            shellVelocity.scaleTo(KART_LENGTH + 0.25*Shell.SIZE)
        );

        return new AddEntity(engineId -> new Shell(
            engineId,
            new EntityStateBuilder().defaults()
                    .shape(Shell.SHAPE)
                    .center(shellCenter)
                    .velocity(shellVelocity)
                    .angularVelocity(Shell.ROTATIONAL_SPEED)
                    .build(),
            getKartId(),
            eventQueue
        ));
    }

}
