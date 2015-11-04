package irl.fw.engine.engine;

import irl.fw.engine.graphics.Renderer;
import irl.fw.engine.collisions.CollisionResolver;
import irl.fw.engine.events.AddBody;
import irl.fw.engine.events.PhysicalEvent;
import irl.fw.engine.events.RemoveBody;
import irl.fw.engine.events.UpdateBody;
import irl.fw.engine.physics.PhysicsModeler;
import irl.fw.engine.physics.impl.NoopPhysicsModeler;
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

    private final Pipe<PhysicalEvent> eventQueue;
    private final PhysicsModeler phyisicsModel;
    private Subscription subscription;

    private final CollisionResolver collisionResolver;
    private final Renderer render;

    Engine(CollisionResolver collisionResolver, Renderer renderer) {
        if (collisionResolver == null || renderer == null) {
            throw new RuntimeException("These fields cannot be null");
        }

        this.eventQueue = new Pipe<>();
        this.phyisicsModel = new NoopPhysicsModeler();

        this.collisionResolver = collisionResolver;
        this.render = renderer;
    }

    @Override
    public void stop() {
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }

    @Override
    public boolean isStopped() {
        return subscription == null || subscription.isUnsubscribed();
    }

    public Pipe<PhysicalEvent> getEventQueue() {
        return eventQueue;
    }

    @Override
    public void run() {
        subscription = eventQueue.get()
            .buffer(TIME_STEP, TimeUnit.MILLISECONDS)
            .timeInterval()
            .subscribe(new Observer<TimeInterval<List<PhysicalEvent>>>() {

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
                public void onNext(TimeInterval<List<PhysicalEvent>> eventBatch) {
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

    public void handleEvent(PhysicalEvent event) {
        //TODO use command pattern instead of hardcoding a method per event
        if (event instanceof AddBody) {
            AddBody addBodyEvent = (AddBody) event;
            String newBodyId = phyisicsModel.addBody((AddBody) event);
            eventQueue.mergeIn(addBodyEvent.getBody().updates(newBodyId));
        }
        else if (event instanceof RemoveBody) {
            phyisicsModel.removeBody((RemoveBody) event);
        }
        else if (event instanceof UpdateBody) {
            phyisicsModel.updateBody((UpdateBody) event);
        }
        else {
            System.err.println("Unhandled or unexpected event: " + event.getName());
        }
    }

    public void updatePhysics(long timeStep) {
        phyisicsModel.model(collisionResolver, timeStep);
    }

    public void render(long timeSinceLastUpdate) {
        render.render(phyisicsModel.getBodies().get(), timeSinceLastUpdate);
    }
}
