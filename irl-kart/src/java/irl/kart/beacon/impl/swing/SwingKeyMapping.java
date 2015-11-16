package irl.kart.beacon.impl.swing;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
            .map(SwingKartInput.FIRE, KeyEvent.VK_X, KeyEvent.VK_Z)
            .build();
    public static final SwingKeyMapping UHJK = new Builder()
            .map(SwingKartInput.UP, KeyEvent.VK_U)
            .map(SwingKartInput.LEFT, KeyEvent.VK_H)
            .map(SwingKartInput.DOWN, KeyEvent.VK_J)
            .map(SwingKartInput.RIGHT, KeyEvent.VK_K)
            .map(SwingKartInput.FIRE, KeyEvent.VK_N, KeyEvent.VK_M)
            .build();
    public static final SwingKeyMapping ARROWS = new Builder()
            .map(SwingKartInput.UP, KeyEvent.VK_UP)
            .map(SwingKartInput.LEFT, KeyEvent.VK_LEFT)
            .map(SwingKartInput.DOWN, KeyEvent.VK_DOWN)
            .map(SwingKartInput.RIGHT, KeyEvent.VK_RIGHT)
            .map(SwingKartInput.FIRE, KeyEvent.VK_ENTER)
            .build();

    private final Map<Integer, SwingKartInput> keyCodeToKartInput;
    private final Map<SwingKartInput, List<Integer>> kartInputToKeyCode;

    private SwingKeyMapping(Map<SwingKartInput, List<Integer>> keyMap) {
        this.kartInputToKeyCode = new HashMap<>(keyMap);

        this.keyCodeToKartInput = new HashMap<>();
        this.kartInputToKeyCode.forEach((kartInput, keyCodes) -> {
            keyCodes.forEach(keyCode -> {
                this.keyCodeToKartInput.put(keyCode, kartInput);
            });
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
        //just return the first, since any will do
        return kartInputToKeyCode.get(input).get(0);
    }

    private static class Builder {

        private Map<SwingKartInput, List<Integer>> keyMap;

        public Builder() {
            keyMap = new HashMap<>();
        }

        public Builder map(SwingKartInput input, int... keyCodes) {
            List<Integer> keyCodeList = new ArrayList<>();
            for (int keyCode : keyCodes) {
                keyCodeList.add(keyCode);
            }
            return map(input, keyCodeList);
        }

        private Builder map(SwingKartInput input, List<Integer> keyCodes) {
            if (!keyMap.containsKey(input)) {
                keyMap.put(input, new ArrayList<>());
            }
            keyMap.get(input).addAll(keyCodes);
            return this;
        }

        public SwingKeyMapping build() {
            return new SwingKeyMapping(keyMap);
        }

    }

}
