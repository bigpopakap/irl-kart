package irl.kart.collisions;

import irl.fw.engine.collisions.CollisionResolver;
import irl.fw.engine.events.EntityCollision;
import irl.kart.entities.Kart;
import irl.kart.entities.Shell;
import irl.kart.entities.items.ItemBox;
import irl.kart.entities.items.ItemBoxPedestal;

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
    public boolean onCollision(EntityCollision collision) {
        //TODO this needs to be made more scalable
        if (collision.isWith(Kart.class, Shell.class)) {
            Kart kart = collision.getType(Kart.class);
            Shell shell = collision.getType(Shell.class);

            kart.spin();
            shell.remove();
            return false;
        }
        else if (collision.isWith(Shell.class, Shell.class)) {
            Shell shell1 = (Shell) collision.getEntity1();
            Shell shell2 = (Shell) collision.getEntity2();

            shell1.remove();
            shell2.remove();
            return false;
        }
        else if (collision.isWith(ItemBox.class)) {
            if (collision.isWith(ItemBox.class, Kart.class)) {
                ItemBox box = collision.getType(ItemBox.class);
                Kart kart = collision.getType(Kart.class);

                kart.takeItem(box.getRandomItem());
                box.remove();
            }

            //item boxes don't interact with anything else
            return false;
        }
        else if (collision.isWith(ItemBoxPedestal.class)) {
            return false; //these never interact
        }
        else {
            return true;
        }
    }

}
