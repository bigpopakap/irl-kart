package irl.kart.beacon.impl;

import java.awt.*;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/14/15
 */
class SwingKartSpinner {

    private final Robot robot;
    private final SwingKeyMapping keyMap;

    public SwingKartSpinner(SwingKeyMapping keyMap) throws AWTException {
        this.keyMap = keyMap;
        robot = new Robot();
    }

    public void doSpin() {
        new Thread(() -> {
            robot.setAutoDelay(10);
            for (int i = 0; i < 20; i++) {
                int lrKeyCode = getRandomLRKeyCode();
                int downKeyCode = keyMap.fromKartInput(SwingKartInput.DOWN);

                robot.keyPress(lrKeyCode);
                robot.keyRelease(lrKeyCode);

                if (i < 5) {
                    robot.keyPress(downKeyCode);
                    robot.keyRelease(downKeyCode);
                }
            }
        }).start();
    }

    private int getRandomLRKeyCode() {
        if (Math.random() < 0.5) {
            return keyMap.fromKartInput(SwingKartInput.LEFT);
        } else {
            return keyMap.fromKartInput(SwingKartInput.RIGHT);
        }
    }

}
