package irl.kart.beacon.impl;

import irl.util.universe.UniverseElementFactory;

import java.awt.*;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/14/15
 */
public class SwingKartFactory implements UniverseElementFactory<SwingKart> {

    private final SwingKeyMapping keyMap;

    public SwingKartFactory(SwingKeyMapping keyMap) {
        this.keyMap = keyMap;
    }

    @Override
    public SwingKart create(String id) {
        try {
            return new SwingKart(id, keyMap);
        } catch (AWTException ex) {
            throw new RuntimeException("Error while creating swing kart: " + id, ex);
        }
    }

}
