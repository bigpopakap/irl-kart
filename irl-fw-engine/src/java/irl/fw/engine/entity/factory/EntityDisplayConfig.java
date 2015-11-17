package irl.fw.engine.entity.factory;

import irl.util.ColorUtils;

import java.awt.*;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/17/15
 */
public class EntityDisplayConfig {

    private String label;
    private Color labelColor;
    private Color outlineColor;
    private Color fillColor;
    private Color velocityColor;

    public EntityDisplayConfig() {
        label("");
        labelColor(Color.BLACK);
        outlineColor(Color.BLACK);
        fillColor(ColorUtils.TRANSPARENT);
        velocityColor(Color.RED);
    }

    public String getLabel() {
        return label;
    }

    public EntityDisplayConfig label(String label) {
        this.label = label;
        return this;
    }

    public Color getLabelColor() {
        return labelColor;
    }

    public EntityDisplayConfig labelColor(Color labelColor) {
        this.labelColor = labelColor;
        return this;
    }

    public Color getOutlineColor() {
        return outlineColor;
    }

    public EntityDisplayConfig outlineColor(Color outlineColor) {
        this.outlineColor = outlineColor;
        return this;
    }

    public Color getFillColor() {
        return fillColor;
    }

    public EntityDisplayConfig fillColor(Color fillColor) {
        this.fillColor = fillColor;
        return this;
    }

    public Color getVelocityColor() {
        return velocityColor;
    }

    public EntityDisplayConfig velocityColor(Color velocityColor) {
        this.velocityColor = velocityColor;
        return this;
    }
}
