package irl.kart.renderer;
import irl.fw.engine.entity.state.EntityStateBuilder;
import irl.fw.engine.entity.state.EntityStateUpdate;
import irl.fw.engine.events.AddEntity;
import irl.fw.engine.events.EngineEvent;
import irl.fw.engine.geometry.Angle;
import irl.fw.engine.geometry.ImmutableShape;
import irl.fw.engine.geometry.Vector2D;
import irl.fw.engine.world.World;
import irl.kart.beacon.KartBeaconEvent;
import irl.kart.entities.items.ItemBoxPedestal;
import irl.kart.events.beacon.UseItem;
import irl.kart.events.beacon.KartStateUpdate;
import irl.fw.engine.graphics.Renderer;
import irl.kart.entities.Kart;
import irl.kart.entities.Wall;
import irl.util.callbacks.Callback;
import irl.util.callbacks.Callbacks;
import irl.util.concurrent.StoppableRunnable;
import irl.util.reactiveio.Pipe;
import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Rectangle2D;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/2/15
 */
public class SwingWorld implements /* KartBeacon,*/ Renderer, StoppableRunnable {

    private static final double WORLD_WIDTH = 1000;
    private static final double WORLD_HEIGHT = 500;

    private static final double WALL_THICKNESS = 20;

    private static final int WINDOW_WIDTH = 1000;
    private static final int WINDOW_HEIGHT = 800;

    private final Pipe<EngineEvent> eventQueue;
    private final String kart1Id;
    private final String kart2Id;

    private final Callbacks onStop;

    private final Subject<KeyEvent, KeyEvent> rawPositions;
    private final Pipe<KartBeaconEvent> updates;

    //pretending to be the "state" of the real world
    private volatile double kart1rot = 0;
    private volatile double kart1speed = 0;
    private volatile double kart2rot = 0;
    private volatile double kart2speed = 0;

    private volatile boolean isStopped = true;
    private JFrame frame;
    private MyPanel panel;

    public SwingWorld(Pipe<EngineEvent> eventQueue, String kart1Id, String kart2Id) {
        this.eventQueue = eventQueue;

        this.kart1Id = kart1Id;
        this.kart2Id = kart2Id;
        this.rawPositions = PublishSubject.<KeyEvent>create().toSerialized();

        this.updates = new Pipe<>();
        this.updates.mergeIn(rawPositions
                .map(this::keyEventToBeaconEvent)
                .filter(update -> update != null));

        onStop = new Callbacks();
    }

    @Override
    public synchronized void stop() {
        if (!isStopped()) {
            frame.dispose();
            isStopped = true;
            onStop.run();
        }
    }

    @Override
    public boolean isStopped() {
        return isStopped;
    }

    @Override
    public String onStop(Callback callback) {
        return onStop.add(callback);
    }

    @Override
    public void run() {
        isStopped = false;

        //add walls and add karts when they are first seen
        final Rectangle2D worldBounds = new Rectangle2D.Double(0, 0, WORLD_WIDTH, WORLD_HEIGHT);
        eventQueue.mergeIn(addWalls(worldBounds));
        eventQueue.mergeIn(addItemBoxes(worldBounds));

        this.frame = new JFrame();
        this.panel = new MyPanel();
        frame.add(panel);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                stop();
            }
        });

        panel.setFocusable(true);
        panel.addKeyListener(new MyKeyListener());

        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        panel.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setVisible(true);
        panel.setVisible(true);
        panel.requestFocusInWindow();
    }

    @Override
    public void render(World world, long timeSinceLastUpdate) {
        if (frame != null && panel != null) {
            panel.update(world);
        }
    }

    private Observable<AddEntity> addWalls(Rectangle2D worldBounds) {
        return Observable.from(new AddEntity[] {

                //left wall
                new AddEntity(entityConfig -> new Wall(
                    entityConfig,
                    new EntityStateBuilder().defaults()
                            .shape(new ImmutableShape(ImmutableShape.Type.RECTANGLE,
                                    new Rectangle2D.Double(0, 0, WALL_THICKNESS, worldBounds.getHeight())))
                            .center(new Vector2D(WALL_THICKNESS / 2, worldBounds.getHeight() / 2))
                            .build()
                )),

                //right wall
                new AddEntity(entityConfig -> new Wall(
                    entityConfig,
                    new EntityStateBuilder().defaults()
                            .shape(new ImmutableShape(ImmutableShape.Type.RECTANGLE,
                                    new Rectangle2D.Double(0, 0, WALL_THICKNESS, worldBounds.getHeight())))
                            .center(new Vector2D(worldBounds.getWidth() - WALL_THICKNESS / 2, worldBounds.getHeight() / 2))
                            .build()
                )),

                //top wall
                new AddEntity(entityConfig -> new Wall(
                    entityConfig,
                    new EntityStateBuilder().defaults()
                            .shape(new ImmutableShape(ImmutableShape.Type.RECTANGLE,
                                    new Rectangle2D.Double(0, 0, WALL_THICKNESS, worldBounds.getWidth())))
                            .rotation(Angle.deg(90))
                            .center(new Vector2D(worldBounds.getWidth()/2, worldBounds.getHeight() - WALL_THICKNESS/2))
                            .build()
                )),

                //bottom wall
                new AddEntity(entityConfig -> new Wall(
                    entityConfig,
                    new EntityStateBuilder().defaults()
                            .shape(new ImmutableShape(ImmutableShape.Type.RECTANGLE,
                                    new Rectangle2D.Double(0, 0, WALL_THICKNESS, worldBounds.getWidth())))
                            .rotation(Angle.deg(90))
                            .center(new Vector2D(worldBounds.getWidth()/2, WALL_THICKNESS/2))
                            .build()
                ))

        });
    }

    private Observable<AddEntity> addItemBoxes(Rectangle2D worldBounds) {
        final double INSET = 4*WALL_THICKNESS;

        return Observable.from(new AddEntity[] {

            //top left
            new AddEntity(entityConfig -> new ItemBoxPedestal(
                entityConfig,
                new EntityStateBuilder().defaults()
                        .center(new Vector2D(INSET, worldBounds.getHeight() - INSET))
                        .shape(ItemBoxPedestal.SHAPE)
                        .build(),
                eventQueue
            )),

            //top right
            new AddEntity(entityConfig -> new ItemBoxPedestal(
                entityConfig,
                new EntityStateBuilder().defaults()
                        .center(new Vector2D(worldBounds.getWidth() - INSET, worldBounds.getHeight() - INSET))
                        .shape(ItemBoxPedestal.SHAPE)
                        .build(),
                eventQueue
            )),

            //bottom left
            new AddEntity(entityConfig -> new ItemBoxPedestal(
                entityConfig,
                new EntityStateBuilder().defaults()
                        .center(new Vector2D(INSET, INSET))
                        .shape(ItemBoxPedestal.SHAPE)
                        .build(),
                eventQueue
            )),

            //bottom right
            new AddEntity(entityConfig -> new ItemBoxPedestal(
                entityConfig,
                new EntityStateBuilder().defaults()
                        .center(new Vector2D(worldBounds.getWidth() - INSET, INSET))
                        .shape(ItemBoxPedestal.SHAPE)
                        .build(),
                eventQueue
            ))

        });
    }

    private KartBeaconEvent keyEventToBeaconEvent(KeyEvent evt) {
        switch (evt.getKeyCode()) {
            /* KART 1 speed */
            case KeyEvent.VK_UP:
                kart1speed = Math.min(Kart.MAX_SPEED, kart1speed + Kart.SPEED_INCR);
                EntityStateUpdate update1speedUp = new EntityStateUpdate()
                        .velocity(new Vector2D(0, kart1speed).rotate(Angle.deg(kart1rot)))
                        .rotation(Angle.deg(kart1rot));
                return new KartStateUpdate(kart1Id, update1speedUp);
            case KeyEvent.VK_DOWN:
                kart1speed = Math.max(Kart.MIN_SPEED, kart1speed - Kart.SPEED_INCR);
                EntityStateUpdate update1speedDown = new EntityStateUpdate()
                        .velocity(new Vector2D(0, kart1speed).rotate(Angle.deg(kart1rot)))
                        .rotation(Angle.deg(kart1rot));
                return new KartStateUpdate(kart1Id, update1speedDown);

            /* KART 1 rotation */
            case KeyEvent.VK_LEFT:
                kart1rot += Kart.ROT_INCR;
                EntityStateUpdate stateUpdateRight = new EntityStateUpdate()
                        .velocity(new Vector2D(0, kart1speed).rotate(Angle.deg(kart1rot)))
                        .rotation(Angle.deg(kart1rot));
                return new KartStateUpdate(kart1Id, stateUpdateRight);
            case KeyEvent.VK_RIGHT:
                kart1rot -= Kart.ROT_INCR;
                EntityStateUpdate stateUpdateLeft = new EntityStateUpdate()
                        .velocity(new Vector2D(0, kart1speed).rotate(Angle.deg(kart1rot)))
                        .rotation(Angle.deg(kart1rot));
                return new KartStateUpdate(kart1Id, stateUpdateLeft);

            /* KART 1 fire weapon */
            case KeyEvent.VK_ENTER:
                return new UseItem(kart1Id);

            /* KART 2 speed */
            case KeyEvent.VK_W:
                kart2speed = Math.min(Kart.MAX_SPEED, kart2speed + Kart.SPEED_INCR);
                EntityStateUpdate update2speedUp = new EntityStateUpdate()
                        .velocity(new Vector2D(0, kart2speed).rotate(Angle.deg(kart2rot)))
                        .rotation(Angle.deg(kart2rot));
                return new KartStateUpdate(kart2Id, update2speedUp);
            case KeyEvent.VK_S:
                kart2speed = Math.max(Kart.MIN_SPEED, kart2speed - Kart.SPEED_INCR);
                EntityStateUpdate update2speedDown = new EntityStateUpdate()
                        .velocity(new Vector2D(0, kart2speed).rotate(Angle.deg(kart2rot)))
                        .rotation(Angle.deg(kart2rot));
                return new KartStateUpdate(kart2Id, update2speedDown);

            /* KART 2 rotation */
            case KeyEvent.VK_A:
                kart2rot += Kart.ROT_INCR;
                EntityStateUpdate stateUpdate2Right = new EntityStateUpdate()
                        .velocity(new Vector2D(0, kart2speed).rotate(Angle.deg(kart2rot)))
                        .rotation(Angle.deg(kart2rot));
                return new KartStateUpdate(kart2Id, stateUpdate2Right);
            case KeyEvent.VK_D:
                kart2rot -= Kart.ROT_INCR;
                EntityStateUpdate stateUpdate2Left = new EntityStateUpdate()
                        .velocity(new Vector2D(0, kart2speed).rotate(Angle.deg(kart2rot)))
                        .rotation(Angle.deg(kart2rot));
                return new KartStateUpdate(kart2Id, stateUpdate2Left);

            /* KART 2 fire weapon */
            case KeyEvent.VK_SPACE:
                return new UseItem(kart2Id);

            default:
                return null;
        }
    }

    private class MyKeyListener implements KeyListener {

        private final Subject<KeyEvent, KeyEvent> keyPresses;
        private final Subject<KeyEvent, KeyEvent> keyReleases;

        public MyKeyListener() {
            keyPresses = PublishSubject.<KeyEvent>create().toSerialized();
            keyReleases = PublishSubject.<KeyEvent>create().toSerialized();

            keyPresses
                .takeUntil(keyReleases)
                .repeat()
                .subscribe(rawPositions);
        }

        @Override
        public void keyTyped(KeyEvent e) {
            //do nothing
        }

        @Override
        public void keyPressed(KeyEvent e) {
            keyPresses.onNext(e);
        }

        @Override
        public void keyReleased(KeyEvent e) {
            keyReleases.onNext(e);
        }

    }

}
