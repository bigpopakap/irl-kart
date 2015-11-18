package irl.kart.beacon.impl.swing;

import irl.fw.engine.entity.state.EntityStateUpdate;
import irl.fw.engine.geometry.Angle;
import irl.fw.engine.geometry.Vector2D;
import irl.kart.beacon.KartBeaconEvent;
import irl.kart.events.beacon.HoldItem;
import irl.kart.events.beacon.KartStateUpdate;
import irl.kart.events.beacon.UseItem;
import irl.kart.events.kart.AdjustKartSpeed;
import irl.kart.events.kart.KartEvent;
import irl.kart.events.kart.SpinKart;

import java.awt.*;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/14/15
 */
class SwingKart {

    private static final double SPEED_INCR = 5;
    private static final Angle ROT_INCR = Angle.deg(15);

    private final String id;
    private final SwingKeyMapping keyMap;

    private static double maxSpeed = 60.0;
    private static final double MIN_SPEED = -30.0;
    private volatile Angle rotation;
    private volatile double speed;

    private final SwingKartSpinner spinner;

    public SwingKart(String id, SwingKeyMapping keyMap) throws AWTException {
        this.id = id;
        this.keyMap = keyMap;

        rotation = Angle.ZERO;
        speed = 0;

        spinner = new SwingKartSpinner(this.keyMap);
    }

    public String getId() {
        return id;
    }

    public KartBeaconEvent handleKeyAndUpdate(SwingKeyEvent keyEvent) {
        if (!keyMap.canHandleKey(keyEvent.getEvent().getKeyCode())) {
            return null;
        }

        SwingKartInput input = keyMap.fromKeyCode(keyEvent.getEvent().getKeyCode());

        boolean isStateUpdate = false;
        switch (input) {
            case LEFT:
                if (keyEvent.getType() == SwingKeyEvent.Type.KEY_DOWN) {
                    rotation = speed >= 0
                            ? rotation.add(ROT_INCR)
                            : rotation.sub(ROT_INCR);
                    isStateUpdate = true;
                }
                break;

            case RIGHT:
                if (keyEvent.getType() == SwingKeyEvent.Type.KEY_DOWN) {
                    rotation = speed >= 0
                            ? rotation.sub(ROT_INCR)
                            : rotation.add(ROT_INCR);
                    isStateUpdate = true;
                }
                break;

            case UP:
                if (keyEvent.getType() == SwingKeyEvent.Type.KEY_DOWN) {
                    speed = Math.min(maxSpeed, speed + SPEED_INCR);
                    isStateUpdate = true;
                }
                break;

            case DOWN:
                if (keyEvent.getType() == SwingKeyEvent.Type.KEY_DOWN) {
                    speed = Math.max(MIN_SPEED, speed - SPEED_INCR);
                    isStateUpdate = true;
                }
                break;

            case FIRE:
                switch (keyEvent.getType()) {
                    case KEY_DOWN: return new HoldItem(getId());
                    case KEY_UP: return new UseItem(getId());
                }

            default:
                return null;
        }

        if (isStateUpdate) {
            EntityStateUpdate update = new EntityStateUpdate()
                    .velocity(new Vector2D(0, speed).rotate(rotation))
                    .rotation(rotation);
            return new KartStateUpdate(getId(), update);
        } else {
            return null;
        }
    }

    public void handleKartEvent(KartEvent kartEvent) {
        if (kartEvent instanceof SpinKart) {
            spin();
        } else if (kartEvent instanceof AdjustKartSpeed) {
            adjustSpeed(((AdjustKartSpeed) kartEvent).getFactor());
        } else {
            System.err.println("Unhandled or unexpected event: " + kartEvent.getName());
        }
    }

    private void spin() {
        spinner.doSpin();
    }

    private void adjustSpeed(double factor) {
        maxSpeed *= factor;
        speed *= factor;
    }

}
