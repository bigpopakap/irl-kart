package irl.kart.entities;

import irl.fw.engine.entity.state.EntityState;
import irl.fw.engine.entity.state.EntityStateBuilder;
import irl.fw.engine.entity.state.EntityStateUpdate;
import irl.fw.engine.events.AddEntity;
import irl.fw.engine.events.EngineEvent;
import irl.fw.engine.geometry.Angle;
import irl.fw.engine.geometry.ImmutableShape;
import irl.fw.engine.geometry.Vector2D;
import irl.kart.beacon.KartBeacon;
import irl.fw.engine.entity.IRLEntity;
import irl.kart.events.kart.SpinKart;
import irl.util.reactiveio.Pipe;
import irl.util.string.StringUtils;
import rx.Observable;

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

    public Kart(String kartId, KartBeacon kartBeacon,
                Pipe<EngineEvent> eventQueue) {
        this.kartId = kartId;
        this.kartBeacon = kartBeacon;
        this.eventQueue = eventQueue;
    }

    public String getKartId() {
        return kartId;
    }

    @Override
    public Observable<EntityStateUpdate> updates() {
        //TODO we should only report the latest position or something
        return kartBeacon.updates()
                .filter(update -> StringUtils.equal(getKartId(), update.getExternalId()))
                .map(update -> update.getStateUpdate());
    }

    public void spin() {
        kartBeacon.send(new SpinKart(getKartId()));
    }

    public void fire(EntityState kartState) {
        Vector2D kartCenter = kartState.getCenter();
        Vector2D kartVelocity = kartState.getVelocity();

        Vector2D shellCenter = kartCenter.add(
            kartVelocity.scaleTo(KART_LENGTH + 2* Shell.SIZE)
        );

        Vector2D shellVelocity = kartVelocity.scaleTo(Shell.SPEED);

        eventQueue.mergeIn(new AddEntity(
            new Shell(getKartId()),
            new EntityStateBuilder()
                    .shape(Shell.SHAPE)
                    .rotation(Angle.deg(0))
                    .center(shellCenter)
                    .velocity(shellVelocity)
                    .build()
        ));
    }

}
