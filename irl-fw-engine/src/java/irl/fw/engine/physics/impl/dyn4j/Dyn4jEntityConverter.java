package irl.fw.engine.physics.impl.dyn4j;

import irl.fw.engine.entity.Entity;
import irl.fw.engine.entity.EntityInstance;
import irl.fw.engine.entity.state.EntityState;
import irl.fw.engine.entity.state.EntityStateBuilder;
import org.dyn4j.dynamics.Body;

import static irl.fw.engine.physics.impl.dyn4j.Dyn4jConverter.toRadAngle;
import static irl.fw.engine.physics.impl.dyn4j.Dyn4jConverter.toVector;
import static irl.fw.engine.physics.impl.dyn4j.Dyn4jShapeConverter.toShape;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/11/15
 */
public class Dyn4jEntityConverter {

    public static EntityInstance toEntity(Body body) {
        Entity entity = (Entity) body.getUserData();

        EntityState state = new EntityStateBuilder()
                .shape(toShape(body.getFixture(0).getShape()))
                .rotation(toRadAngle(body.getTransform().getRotation()))
                .center(toVector(body.getWorldCenter()))
                .velocity(toVector(body.getLinearVelocity()))
                .build();

        return new EntityInstance(entity, state);
    }

}
