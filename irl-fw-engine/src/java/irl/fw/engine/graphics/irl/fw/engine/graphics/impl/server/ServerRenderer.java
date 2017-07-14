package irl.fw.engine.graphics.irl.fw.engine.graphics.impl.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import irl.fw.engine.graphics.Renderer;
import irl.fw.engine.world.World;
import irl.util.callbacks.Callback;
import irl.util.callbacks.Callbacks;
import irl.util.concurrent.StoppableRunnable;

import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 7/14/17
 */
public class ServerRenderer implements Renderer, StoppableRunnable {

    private static final String PATH = "/world";
    private static final int PORT = 8080;
    private static final String URL = "http://localhost:" + PORT + PATH;

    private boolean isStopped = false;
    private final Callbacks onStop = new Callbacks();

    private final HttpServer server;
    // NOTE: this is an array so that the reference doesn't change
    // and we can therefore do a synchronized() on this variable
    private final String[] serverResponse = new String[] { "" };

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

    private String getServerResponse() {
        return serverResponse[0];
    }

    private void setServerResponse(String serverResponse) {
        this.serverResponse[0] = serverResponse;
    }

    @Override
    public void run() {
        server.start();

        // Open the browser to this tab
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(new URI(URL));
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void render(World world, long timeSinceLastUpdate) {
        synchronized (serverResponse) {
            setServerResponse(world.toJSON());
        }
    }

    private class WorldHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange t) throws IOException {
            synchronized (serverResponse) {
                t.sendResponseHeaders(200, getServerResponse().getBytes().length);
                OutputStream os = t.getResponseBody();
                os.write(getServerResponse().getBytes());
                os.close();
            }
        }

    }

}
