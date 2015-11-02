package irl.kart;

import irl.fw.physics.events.AddBody;
import irl.fw.physics.events.PhysicalEvent;
import irl.fw.physics.runner.Simulator;
import irl.fw.physics.world.World;
import irl.kart.beacon.AsyncRandomKartBeacon;
import irl.kart.bodies.TestBody;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/1/15
 */
public class Main {

    public static void main(String[] args) throws Exception {
        final String KART_1_ID = "kart1";
        final String KART_2_ID = "kart2";

        AsyncRandomKartBeacon beacon = new AsyncRandomKartBeacon(KART_1_ID, KART_2_ID);
        World world = new World();
        Simulator<PhysicalEvent> worldSim = new Simulator<>(world);

        //start initializing the world
        //TODO can we avoid having to declare these karts up front?
        worldSim.prepare();
        world.handleEvent(new AddBody(new TestBody(KART_1_ID, beacon)));
        world.handleEvent(new AddBody(new TestBody(KART_2_ID, beacon)));

        new Thread(worldSim).start();
        new Thread(beacon).start();

        //kill it after a little bit
        Thread.sleep(10000);
        beacon.stop();
        worldSim.stop();
    }

}
