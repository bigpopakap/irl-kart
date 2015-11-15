package irl.kart.beacon.impl;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/14/15
 */
class SwingKeyMapping {

    public static final SwingKeyMapping WASD = new Builder()
            .map(SwingKartInput.UP, KeyEvent.VK_W)
            .map(SwingKartInput.LEFT, KeyEvent.VK_A)
            .map(SwingKartInput.DOWN, KeyEvent.VK_S)
            .map(SwingKartInput.RIGHT, KeyEvent.VK_D)
            .map(SwingKartInput.FIRE, KeyEvent.VK_E)
            .build();
    public static final SwingKeyMapping UHJK = new Builder()
            .map(SwingKartInput.UP, KeyEvent.VK_U)
            .map(SwingKartInput.LEFT, KeyEvent.VK_H)
            .map(SwingKartInput.DOWN, KeyEvent.VK_J)
            .map(SwingKartInput.RIGHT, KeyEvent.VK_K)
            .map(SwingKartInput.FIRE, KeyEvent.VK_I)
            .build();
    public static final SwingKeyMapping ARROWS = new Builder()
            .map(SwingKartInput.UP, KeyEvent.VK_UP)
            .map(SwingKartInput.LEFT, KeyEvent.VK_LEFT)
            .map(SwingKartInput.DOWN, KeyEvent.VK_DOWN)
            .map(SwingKartInput.RIGHT, KeyEvent.VK_RIGHT)
            .map(SwingKartInput.FIRE, KeyEvent.VK_ENTER)
            .build();

    private final Map<Integer, SwingKartInput> keyCodeToKartInput;
    private final Map<SwingKartInput, Integer> kartInputToKeyCode;

    private SwingKeyMapping(Map<SwingKartInput, Integer> keyMap) {
        this.kartInputToKeyCode = new HashMap<>(keyMap);

        this.keyCodeToKartInput = new HashMap<>();
        this.kartInputToKeyCode.forEach((kartInput, keyCode) -> {
            this.keyCodeToKartInput.put(keyCode, kartInput);
        });

        //check that all inputs are in the map
        for (SwingKartInput input : SwingKartInput.values()) {
            if (!this.kartInputToKeyCode.containsKey(input)) {
                throw new RuntimeException("Developer error!!: This key mapping doesn't handle input: " + input);
            }
        }
    }

    public boolean canHandleKey(int keyCode) {
        return keyCodeToKartInput.containsKey(keyCode);
    }

    public SwingKartInput fromKeyCode(int keyCode) {
        if (!canHandleKey(keyCode)) {
            throw new RuntimeException("This key mapping doesn't have an entry for key: " + keyCode);
        }
        return keyCodeToKartInput.get(keyCode);
    }

    public int fromKartInput(SwingKartInput input) {
        return kartInputToKeyCode.get(input);
    }

    private static class Builder {

        private Map<SwingKartInput, Integer> keyMap;

        public Builder() {
            keyMap = new HashMap<>();
        }

        public Builder map(SwingKartInput input, int keyCode) {
            keyMap.put(input, keyCode);
            return this;
        }

        public SwingKeyMapping build() {
            return new SwingKeyMapping(keyMap);
        }

    }

}
