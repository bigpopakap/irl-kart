package irl.fw.physics.runner;

import irl.util.concurrent.Looper;

import java.util.concurrent.TimeUnit;

/**
 * TODO bigpopakap Javadoc this class
 *
 * Copied from http://gameprogrammingpatterns.com/game-loop.html
 *
 * @author bigpopakap
 * @since 10/31/15
 */
public class SimulationLooper extends Looper {

    private static final long DEFAULT_MILLIS_PER_UPDATE = 33; //roughly 30fps
    private static final long DEFAULT_MIN_MILLIS_PER_UPDATE = 8; //roughly 120fps

    private final long millisPerUpdate;
    private final long minMillisPerUpdate;
    private final Simulatable simulatable;
    
    private long previous;
    private TimeUnit timeUnit;
    private long lag;

    public SimulationLooper(Simulatable simulatable) {
        this.millisPerUpdate = DEFAULT_MILLIS_PER_UPDATE;
        this.minMillisPerUpdate = DEFAULT_MIN_MILLIS_PER_UPDATE;
        this.simulatable = simulatable;
    }

    @Override
    protected void beforeLoop() {
        previous = now();
        timeUnit = nowUnit();
        lag = 0;
    }

    @Override
    public void loopIteration() {
        long current = now();
        long elapsed = current - previous;
        previous = current;
        lag += elapsed;

        simulatable.processInput(millisPerUpdate, timeUnit);

        while (lag >= millisPerUpdate) {
            simulatable.updatePhysics(millisPerUpdate, timeUnit);
            lag -= millisPerUpdate;
        }

        simulatable.render(lag / millisPerUpdate, timeUnit);

        try {
            long actualTime = now() - previous;
            if (actualTime < minMillisPerUpdate) {
                Thread.sleep(minMillisPerUpdate - actualTime);
            }
        } catch (InterruptedException ex) {
            //continue
        }
    }

    @Override
    public boolean isStopped() {
        return super.isStopped() || simulatable.isDone();
    }

    private long now() {
        return System.currentTimeMillis();
    }

    private TimeUnit nowUnit() {
        return TimeUnit.MILLISECONDS;
    }

}
