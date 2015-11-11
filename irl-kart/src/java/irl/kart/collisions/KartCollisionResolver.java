package irl.kart.collisions;

import irl.fw.engine.collisions.CollisionResolver;
import irl.fw.engine.entity.EntityInstance;
import irl.fw.engine.events.EntityCollision;
import irl.kart.entities.Kart;
import irl.kart.entities.Shell;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/11/15
 */
public class KartCollisionResolver implements CollisionResolver {

    public KartCollisionResolver() {
        //do nothing
    }

    @Override
    public boolean onBeforeCollision(EntityCollision collision) {
        return true;
    }

    @Override
    public void onCollision(EntityCollision collision) {
        if (collision.isBetween(Kart.class, Shell.class)) {
            EntityInstance kartInst = collision.getType(Kart.class);
            Kart kart = (Kart) kartInst.getEntity();
            kart.spin();
        }
    }

}
