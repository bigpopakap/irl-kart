package irl.fw.engine.engine;

import irl.fw.engine.events.*;
import irl.fw.engine.graphics.Renderer;
import irl.fw.engine.collisions.CollisionResolver;
import irl.fw.engine.physics.PhysicsModeler;
import irl.util.callbacks.Callback;
import irl.util.callbacks.Callbacks;
import irl.util.concurrent.StoppableRunnable;
import irl.util.reactiveio.EventQueue;
import rx.Observable;
import rx.Subscription;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 10/31/15
 */
public class Engine implements StoppableRunnable {

    private static final long TIME_STEP = 33; //roughly 30fps

    private final Callbacks onStop;

    private final EventQueue<EngineEvent> eventQueue;
    private final PhysicsModeler phyisicsModel;
    private Subscription subscription;

    private final CollisionResolver collisionResolver;
    private final Renderer renderer;

    Engine(Observable<? extends EngineEvent> extraEvents,
            PhysicsModeler phyisicsModel, CollisionResolver collisionResolver,
            Renderer renderer) {
        if (phyisicsModel == null || collisionResolver == null || renderer == null) {
            throw new RuntimeException("These fields cannot be null");
        }

        this.onStop = new Callbacks();

        this.eventQueue = new EventQueue<>();
        if (extraEvents != null) {
            this.eventQueue.mergeIn(extraEvents);
        }

        this.phyisicsModel = phyisicsModel;
        this.collisionResolver = collisionResolver;
        this.renderer = renderer;
    }

    @Override
    public synchronized void stop() {
        if (!isStopped()) {
            subscription.unsubscribe();
            subscription = null;
            onStop.run();
        }
    }

    @Override
    public boolean isStopped() {
        return subscription == null;
    }

    @Override
    public String onStop(Callback callback) {
        return onStop.add(callback);
    }

    @Override
    public void run() {
        Semaphore waitForUpdate = new Semaphore(0);
        Semaphore waitForRender = new Semaphore(1);
        AtomicLong lastUpdated = new AtomicLong(0);

        subscription = eventQueue.get().subscribe(this::handleEvent);

        Observable.interval(TIME_STEP, TimeUnit.MILLISECONDS)
                .subscribe(num -> {
                    this.updatePhysics(TIME_STEP);
                    lastUpdated.set(System.currentTimeMillis());

                    if (waitForRender.availablePermits() > 0) {
                        waitForUpdate.release();
                    }
                });

        new Thread(() -> {
//            long lastRender = 0;
            while (true) {
                try {
                    waitForUpdate.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                long currentRender = System.currentTimeMillis();
                long ellapsedSinceUpdate = currentRender - lastUpdated.get();

//                //print stuff out for debugging
//                long ellapsedSinceLastRender = currentRender - lastRender;
//                System.out.println(String.format(
//                    "Rendering (%s since render, %s since update)",
//                    ellapsedSinceLastRender,
//                    ellapsedSinceUpdate
//                ));

                this.render(ellapsedSinceUpdate);
//                lastRender = currentRender;

                waitForRender.release();
            }
        }).start();
    }

    public synchronized void handleEvent(EngineEvent event) {
        //TODO use command pattern instead of hardcoding a method per event
        if (event instanceof AddEntity) {
            phyisicsModel.addEntity((AddEntity) event);
        } else if (event instanceof AddJoint) {
            phyisicsModel.addJoint((AddJoint) event);
        } else if (event instanceof RemoveEntity) {
            phyisicsModel.removeEntity((RemoveEntity) event);
        } else if (event instanceof UpdateEntity) {
            phyisicsModel.updateEntity((UpdateEntity) event);
        } else {
            System.err.println("Unhandled or unexpected event: " + event.getName());
        }
    }

    public synchronized void updatePhysics(long timeStep) {
        phyisicsModel.model(collisionResolver, timeStep);
    }

    public synchronized void render(long timeSinceLastUpdate) {
        renderer.render(phyisicsModel.getWorld(), timeSinceLastUpdate);
    }
}
