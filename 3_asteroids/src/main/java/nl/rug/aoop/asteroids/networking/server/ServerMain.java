package nl.rug.aoop.asteroids.networking.server;

import java.net.SocketException;

/**
 * Server Main Class
 */
public class ServerMain {

    /**
     * Class constructor.
     * <p>
     * It spawns a new thread to handle incoming connections.
     */
    public static Server startServer() {
        Server server = null;
        try {
            server = new Server();
            Thread thread = new Thread(server);
            thread.start();
        } catch (SocketException e) {
            System.out.println("Server couldn't create Server Engine");
        }
        return server;
    }
}
