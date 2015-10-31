package irl.fw.physics.runner;

import java.util.concurrent.TimeUnit;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 10/31/15
 */
public class Looper implements Runnable {

    private static final long DEFAULT_MILLIS_PER_UPDATE = 33; //roughly 30fps

    private final long millisPerUpdate;
    private final Loopable loopable;

    public Looper(Loopable loopable) {
        this(DEFAULT_MILLIS_PER_UPDATE, loopable);
    }

    public Looper(long millisPerUpdate, Loopable loopable) {
        this.millisPerUpdate = millisPerUpdate;
        this.loopable = loopable;
    }

    @Override
    public void run() {
        /*
         * Copied from http://gameprogrammingpatterns.com/game-loop.html
         */
        long previous = now();
        long lag = 0;
        while (!loopable.isDone()) {
            long current = now();
            long elapsed = current - previous;
            previous = current;
            lag += elapsed;

            while (lag >= millisPerUpdate) {
                loopable.eventQueue()
                        .take(millisPerUpdate, TimeUnit.MILLISECONDS)
                        .subscribe(loopable);
                lag -= millisPerUpdate;
            }

            loopable.render(lag / millisPerUpdate);
        }
    }

    private long now() {
        return System.currentTimeMillis();
    }

}
