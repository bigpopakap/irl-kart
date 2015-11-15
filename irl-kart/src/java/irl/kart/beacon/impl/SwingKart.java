package irl.kart.beacon.impl;

import irl.fw.engine.entity.state.EntityStateUpdate;
import irl.fw.engine.geometry.Angle;
import irl.fw.engine.geometry.Vector2D;
import irl.kart.beacon.KartBeaconEvent;
import irl.kart.events.beacon.KartStateUpdate;
import irl.kart.events.beacon.UseItem;

import java.awt.*;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/14/15
 */
class SwingKart {

    private static final double MAX_SPEED = 120;
    private static final double MIN_SPEED = -60;
    private static final double SPEED_INCR = 20;
    private static final Angle ROT_INCR = Angle.deg(15);

    private final String id;
    private final SwingKeyMapping keyMap;

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

    public KartBeaconEvent handleKeyAndUpdate(int keyCode) {
        if (!keyMap.canHandleKey(keyCode)) {
            return null;
        }

        SwingKartInput input = keyMap.fromKeyCode(keyCode);

        boolean isStateUpdate;
        switch (input) {
            case LEFT:
                rotation = speed >= 0
                        ? rotation.add(ROT_INCR)
                        : rotation.sub(ROT_INCR);
                isStateUpdate = true;
                break;

            case RIGHT:
                rotation = speed >= 0
                        ? rotation.sub(ROT_INCR)
                        : rotation.add(ROT_INCR);
                isStateUpdate = true;
                break;

            case UP:
                speed = Math.min(MAX_SPEED, speed + SPEED_INCR);
                isStateUpdate = true;
                break;

            case DOWN:
                speed = Math.max(MIN_SPEED, speed - SPEED_INCR);
                isStateUpdate = true;
                break;

            case FIRE:
                return new UseItem(getId());

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

    public void spin() {
        spinner.doSpin();
    }

}
