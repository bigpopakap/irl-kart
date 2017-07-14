package irl.fw.engine.graphics.irl.fw.engine.graphics.impl.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import irl.fw.engine.graphics.Renderer;
import irl.fw.engine.world.World;
import irl.util.callbacks.Callback;
import irl.util.callbacks.Callbacks;
import irl.util.concurrent.StoppableRunnable;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 7/14/17
 */
public class ServerRenderer implements Renderer, StoppableRunnable {

    private static final String PATH = "/world";
    private static final int PORT = 8080;

    private boolean isStopped = false;
    private final Callbacks onStop = new Callbacks();

    private final HttpServer server;
    private String serverResponse = "";

    public ServerRenderer() throws IOException {
        server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext(PATH, new WorldHandler());
    }

    @Override
    public void stop() {
        if (!isStopped()) {
            server.stop(0);
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
        server.start();
    }

    @Override
    public void render(World world, long timeSinceLastUpdate) {
        synchronized (serverResponse) {
            serverResponse = world.toJSON();
        }
    }

    private class WorldHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange t) throws IOException {
            synchronized (serverResponse) {
                t.sendResponseHeaders(200, serverResponse.getBytes().length);
                OutputStream os = t.getResponseBody();
                os.write(serverResponse.getBytes());
                os.close();
            }
        }

    }

}
