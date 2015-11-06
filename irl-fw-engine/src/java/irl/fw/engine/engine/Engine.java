package irl.fw.engine.engine;

import irl.fw.engine.entity.Entity;
import irl.fw.engine.graphics.Renderer;
import irl.fw.engine.collisions.CollisionResolver;
import irl.fw.engine.events.AddEntity;
import irl.fw.engine.events.EngineEvent;
import irl.fw.engine.events.RemoveEntity;
import irl.fw.engine.events.UpdateEntity;
import irl.fw.engine.physics.PhysicsModeler;
import irl.util.callbacks.Callback;
import irl.util.callbacks.Callbacks;
import irl.util.concurrent.StoppableRunnable;
import irl.util.reactiveio.Pipe;
import rx.Observer;
import rx.Subscription;
import rx.schedulers.TimeInterval;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 10/31/15
 */
public class Engine implements StoppableRunnable {

    private static final long TIME_STEP = 33; //roughly 30fps

    private final Callbacks onStop;

    private final Pipe<EngineEvent> eventQueue;
    private final PhysicsModeler phyisicsModel;
    private Subscription subscription;

    private final CollisionResolver collisionResolver;
    private final Renderer renderer;

    Engine(PhysicsModeler phyisicsModel, CollisionResolver collisionResolver, Renderer renderer) {
        if (phyisicsModel == null || collisionResolver == null || renderer == null) {
            throw new RuntimeException("These fields cannot be null");
        }

        this.onStop = new Callbacks();

        this.eventQueue = new Pipe<>();
        this.phyisicsModel = phyisicsModel;

        this.collisionResolver = collisionResolver;
        this.renderer = renderer;
    }

    @Override
    public synchronized void stop() {
        if (!isStopped()) {
            subscription.unsubscribe();
            onStop.run();
        }
    }

    @Override
    public boolean isStopped() {
        return subscription == null || subscription.isUnsubscribed();
    }

    @Override
    public String onStop(Callback callback) {
        return onStop.add(callback);
    }

    public Pipe<EngineEvent> getEventQueue() {
        return eventQueue;
    }

    @Override
    public void run() {
        subscription = eventQueue.get()
            .serialize()
            .buffer(TIME_STEP, TimeUnit.MILLISECONDS)
            .timeInterval()
            .subscribe(new Observer<TimeInterval<List<EngineEvent>>>() {

                private volatile long lag = 0;

                @Override
                public void onCompleted() {
                    //do nothing
                }

                @Override
                public void onError(Throwable e) {
                    e.printStackTrace();
                }

                @Override
                public void onNext(TimeInterval<List<EngineEvent>> eventBatch) {
                    long elapsed = eventBatch.getIntervalInMilliseconds();
                    lag += elapsed;

                    //process the batched inputs
                    //TODO is there a way to do this with observables instead of just iterating?
                    eventBatch.getValue().forEach(Engine.this::handleEvent);

                    //TODO is there a way to do this more function-oriented?
                    while (lag > TIME_STEP) {
                        Engine.this.updatePhysics(TIME_STEP);
                        lag -= TIME_STEP;
                    }

                    Engine.this.render(lag / TIME_STEP);
                }

            });
    }

    public void handleEvent(EngineEvent event) {
        //TODO use command pattern instead of hardcoding a method per event
        if (event instanceof AddEntity) {
            AddEntity addEntityEvent = (AddEntity) event;
            Entity newEntity = ((AddEntity) event).getEntity();

            String newEntityId = phyisicsModel.add(addEntityEvent);
            eventQueue.mergeIn(newEntity.updates()
                    .map(state -> new UpdateEntity(newEntityId, state)));
        }
        else if (event instanceof RemoveEntity) {
            phyisicsModel.remove((RemoveEntity) event);
        }
        else if (event instanceof UpdateEntity) {
            phyisicsModel.update((UpdateEntity) event);
        }
        else {
            System.err.println("Unhandled or unexpected event: " + event.getName());
        }
    }

    public void updatePhysics(long timeStep) {
        phyisicsModel.model(collisionResolver, timeStep);
    }

    public void render(long timeSinceLastUpdate) {
        renderer.render(phyisicsModel.getEntities(), timeSinceLastUpdate);
    }
}
