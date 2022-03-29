package nl.rug.aoop.asteroids.networking.client;

import javax.swing.*;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Main class for clients
 */
public class ClientMain {

    /**
     * It starts a client asking for a port to connect to
     *
     * @param nickname of the player
     * @return the created Client
     * @throws SocketException throws exception if the socket fails
     */
    public static Client startClient(String nickname) throws SocketException, UnknownHostException {
        String ip = getIP();
        int port = getPort();
        InetAddress inetAddress = InetAddress.getByName(ip);
        Client client = new Client(port, nickname, inetAddress);
        Thread thread = new Thread(client);
        thread.start();
        return client;
    }

    private static int getPort() {
        String portString = JOptionPane.showInputDialog("Provide server port: ");
        return Integer.parseInt(portString);
    }

    private static String getIP() {
        return JOptionPane.showInputDialog("Provide server ip: ");
    }
}
