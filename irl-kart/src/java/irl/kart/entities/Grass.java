package irl.kart.entities;

import irl.fw.engine.entity.factory.EntityConfig;
import irl.fw.engine.entity.factory.EntityDisplayConfig;
import irl.fw.engine.entity.state.EntityState;
import irl.kart.entities.surface.SpeedAdjustingSurface;
import irl.util.ColorUtils;

import java.awt.*;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/18/15
 */
public class Grass extends SpeedAdjustingSurface {

    private static final double SLOW_FACTOR = .75;
    private static final Color COLOR = new Color(163, 255, 74);
    private static final EntityDisplayConfig DISPLAY = new EntityDisplayConfig()
            .outlineColor(ColorUtils.TRANSPARENT)
            .fillColor(COLOR);

    public Grass(EntityConfig entityConfig, EntityState initState) {
        super(entityConfig.display(DISPLAY), initState);
    }

    @Override
    protected double slowFactor() {
        return SLOW_FACTOR;
    }

}
