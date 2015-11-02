package irl.kart;

import irl.fw.physics.runner.SimulationLooper;
import irl.fw.physics.world.World;
import irl.fw.physics.world.WorldBuilder;
import irl.kart.beacon.HardcodedKartBeacon;
import irl.kart.bodies.TestBody;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/1/15
 */
public class Main {

    public static void main(String[] args) throws Exception {
        HardcodedKartBeacon beacon = new HardcodedKartBeacon();

        //build the world
        World world = new WorldBuilder()
                .addBody(new TestBody(beacon))
                .addBody(new TestBody(beacon))
                .build();

        //start the world in a new thread
        SimulationLooper worldLoop = new SimulationLooper(world);

        new Thread(worldLoop).start();
        new Thread(beacon).start();

        //kill it after a little bit
        Thread.sleep(5000);
        beacon.stop();
        worldLoop.stop();
    }

    //TODO remove this method when done using it for tests
//    private static void testPublisher() throws InterruptedException {
//        PublishSubject<String> queue = PublishSubject.create();
//        queue.buffer(1500, TimeUnit.MILLISECONDS)
//                .subscribe((str) -> System.out.println(str));
//
//        new Thread(() -> {
//            for (int i = 0; i < 10; i++) {
//                queue.onNext("Iteration " + i);
//
//                try {
//                    Thread.sleep(500);
//                } catch (InterruptedException e) {
//                    //do nothing
//                }
//            }
//        }).start();
//
//        Thread.sleep(5000);
//    }

}
