package irl.kart.renderer;

import irl.fw.engine.graphics.Renderer;
import irl.fw.engine.world.World;
import irl.util.callbacks.Callback;
import irl.util.callbacks.Callbacks;
import irl.util.concurrent.StoppableRunnable;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 11/14/15
 */
public class SwingRenderer implements Renderer, StoppableRunnable {

    private static final int WINDOW_WIDTH = 1000;
    private static final int WINDOW_HEIGHT = 800;

    private volatile boolean isStopped = true;
    private final Callbacks onStop;

    private final JFrame frame;
    private final MyPanel panel;

    public SwingRenderer() {
        onStop = new Callbacks();

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
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        panel.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
    }

    public JPanel getPanel() {
        return panel;
    }

    @Override
    public void run() {
        isStopped = false;

        frame.setVisible(true);
        panel.setVisible(true);
        panel.requestFocusInWindow();
    }

    @Override
    public void render(World world, long timeSinceLastUpdate) {
        panel.update(world);
    }

    @Override
    public void stop() {
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

}
