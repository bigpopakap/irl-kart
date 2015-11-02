package irl.kart;

import irl.fw.shared.events.AddBody;
import irl.fw.shared.events.PhysicalEvent;
import irl.fw.engine.runner.Simulator;
import irl.fw.engine.world.World;
import irl.fw.engine.world.WorldBuilder;
import irl.fw.beacon.beacons.AsyncRandomBeacon;
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

        AsyncRandomBeacon beacon = new AsyncRandomBeacon(KART_1_ID, KART_2_ID);
        World world = new WorldBuilder().build();
        Simulator<PhysicalEvent> worldSim = new Simulator<>(world);

        //start initializing the world
        //FIXME can we avoid having to declare these karts up front?
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
