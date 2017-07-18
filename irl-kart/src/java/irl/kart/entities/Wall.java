package irl.kart.entities;

import irl.fw.engine.entity.IRLEntity;
import irl.fw.engine.entity.factory.EntityConfig;
import irl.fw.engine.entity.factory.EntityDisplayConfig;
import irl.fw.engine.entity.state.EntityState;

import java.awt.*;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/10/15
 */
public class Wall extends IRLEntity {

    private static final EntityDisplayConfig DISPLAY = new EntityDisplayConfig()
            .outlineColor(Color.DARK_GRAY)
            .fillColor(Color.GRAY);

    public Wall(EntityConfig entityConfig, EntityState initState) {
        super(
            entityConfig.display(DISPLAY),
            initState
        );
    }

    @Override
    public String getEntityDisplayType() {
        return "Wall";
    }

}
