package nl.rug.aoop.asteroids.networking.server;

import lombok.Getter;
import nl.rug.aoop.asteroids.control.GameController;
import nl.rug.aoop.asteroids.networking.Data;
import nl.rug.aoop.asteroids.networking.PacketHandler;
import nl.rug.aoop.asteroids.view.ViewController;

import java.io.IOException;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Class that represents a server
 */
public class Server extends PacketHandler implements Runnable {
    private int threadNr;
    private boolean running;
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final ServerEngine serverEngine = new ServerEngine();
    private final ViewController viewController = ViewController.getInstance();
    private final GameController gameController = GameController.getInstance();

    @Getter
    private static final Map<Integer, ClientHandler> clientHandlers = new HashMap<>();

    /**
     * Server Constructor
     */
    public Server() throws SocketException {
        this.threadNr = 1;
        this.running = false;

        ClientHandler.setNicknames(new HashMap<>());

        Thread thread = new Thread(serverEngine);
        thread.start();
    }

    private void handleIncomingRequests(DatagramSocket datagramSocket) {
        try {
            DatagramPacket initialPacket = receive(datagramSocket);
            var connectionDetails = new InetSocketAddress(initialPacket.getAddress(), initialPacket.getPort());

            Data data = receiveData(initialPacket);
            if (data.getObjectType() == Data.PLAYER_CONNECTED) {
                String nickName = (String) data.getBody();
                ClientHandler.addNickname(threadNr, nickName);
                gameController.addPlayer(nickName, threadNr);
            }
            ClientHandler clientHandler = new ClientHandler(connectionDetails, threadNr, serverEngine.getPort());
            clientHandlers.put(threadNr, clientHandler);
            executorService.submit(clientHandler);
            threadNr++;
        } catch (IOException e) {
            System.out.println("Server: Error receiving initial packet");
        }
    }

    /**
     * run method of the thread, it keeps listening the in socket waiting for new clients
     */
    @Override
    public void run() {
        try (DatagramSocket datagramSocket = new DatagramSocket(0)) {

            String ip = InetAddress.getLocalHost().getHostAddress();

            viewController.setIP(ip);
            viewController.setPort(datagramSocket.getLocalPort());
            running = true;

            while (running) {
                handleIncomingRequests(datagramSocket);
            }
            serverEngine.closeServerEngine();

        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void closeServer() {
        clientHandlers.forEach((integer, clientHandler) -> clientHandler.closeClientHandler());
        running = false;
    }

    public void startGame() {
        clientHandlers.forEach((integer, clientHandler) -> clientHandler.startGame());
    }

    public static void playerDisconnected(String nickname, Integer threadNr) {
        clientHandlers.forEach((integer, clientHandler) -> clientHandler.playerDisconnected(nickname, threadNr));
    }
}
