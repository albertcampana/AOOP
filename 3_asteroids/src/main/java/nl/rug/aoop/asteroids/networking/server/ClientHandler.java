package nl.rug.aoop.asteroids.networking.server;

import lombok.Getter;
import lombok.Setter;
import nl.rug.aoop.asteroids.control.GameController;
import nl.rug.aoop.asteroids.model.Game;
import nl.rug.aoop.asteroids.networking.Data;
import nl.rug.aoop.asteroids.networking.PacketHandler;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

/**
 * Class that handles clients
 */
public class ClientHandler extends PacketHandler implements Runnable {
    private static final int SLEEP_TIME = 100;
    private DatagramSocket datagramSocket;
    private final InetSocketAddress connectionDetails;
    private boolean running;
    private boolean gameStarted;
    private final int threadNr;
    private final int serverEnginePort;
    private final Game game = GameController.getGame();

    @Getter
    @Setter
    private static Map<Integer, String> nicknames = new HashMap<>();

    /**
     * Constructor
     *
     * @param connectionDetails InetSocketAddress that listens the client
     * @param threadNr          thread number
     */
    public ClientHandler(InetSocketAddress connectionDetails, int threadNr, int serverEnginePort) {
        try {
            datagramSocket = new DatagramSocket(0);
        } catch (SocketException e) {
            System.out.println("ClientHandler: Exception while creating DatagramSocket");
        }
        this.connectionDetails = connectionDetails;
        this.threadNr = threadNr;
        this.serverEnginePort = serverEnginePort;
        this.running = false;
        this.gameStarted = false;
    }

    /**
     * Method that adds a new client's nickname to the nickname array
     *
     * @param threadNr threadNr of the client handler of the new client
     * @param nickname nickname of the new client
     */
    public static void addNickname(Integer threadNr, String nickname) {
        nicknames.put(threadNr, nickname);
    }

    /**
     * Method that removes a client's nickname from the nickname array
     *
     * @param threadNr threadNr of the client handler of the client
     */
    public static void removeNickname(Integer threadNr) {
        nicknames.remove(threadNr);
    }

    /**
     * When an object implementing interface {@code Runnable} is used
     * to create a thread, starting the thread causes the object's
     * {@code run} method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method {@code run} is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        sendConnectionPacket();
        running = true;

        while (running && !gameStarted) {
            sendPlayerNames();
        }
        while (running) {
            sendGameStatus();
            threadSleep();
        }
    }

    private void threadSleep() {
        try {
            Thread.sleep(30);
        } catch (InterruptedException e) {
            System.out.println("ClientHandler: thread sleep");
        }
    }

    private void sendConnectionPacket() {
        Data data = new Data();
        data.setObjectType(Data.PLAYER_CONNECTED);
        data.setThreadNr(threadNr);
        data.setBody(serverEnginePort);
        sendData(data);
    }

    private void sendPlayerNames() {
        Data data = new Data();
        data.setObjectType(Data.PLAYER_CONNECTED);

        for (var entry : nicknames.entrySet()) {
            data.setThreadNr(entry.getKey());
            data.setBody(entry.getValue());
            sendData(data);
        }

        try {
            Thread.sleep(SLEEP_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void sendGameStatus() {
        Data data = new Data();
        data.setObjectType(Data.SENDING_GAME_STATUS);
        sendData(data);

        sendGameObjects();

        data.setObjectType(Data.GAME_STATUS_SENT);
        sendData(data);
    }

    private void endGame() {
        Data data = new Data();
        data.setObjectType(Data.ABORT_GAME);
        sendData(data);
        datagramSocket.close();
    }

    /**
     * Method that closes the ClientHandler
     */
    public void closeClientHandler() {
        running = false;
        endGame();
    }

    /**
     * Method that prepares the client handler for the game
     */
    public void startGame() {
        Data data = new Data();
        data.setObjectType(Data.START_GAME);
        sendData(data);

        sendGameObjects();

        gameStarted = true;
    }

    private void sendGameObjects() {
        sendAsteroids();

        sendBullets();

        sendSpaceships();
    }

    private void sendSpaceships() {
        Data data = new Data();
        data.setObjectType(Data.SPACESHIP);
        for (var entry : game.getSpaceshipMap().entrySet()) {
            data.setThreadNr(entry.getKey());
            data.setBody(entry.getValue());
            sendData(data);
        }
    }

    private void sendBullets() {
        Data data = new Data();
        data.setObjectType(Data.BULLET);
        game.getBullets().forEach(bullet -> {
            data.setBody(bullet);
            sendData(data);
        });
    }

    private void sendAsteroids() {
        Data data = new Data();
        data.setObjectType(Data.ASTEROID);
        game.getAsteroids().forEach(asteroid -> {
            data.setBody(asteroid);
            sendData(data);
        });
    }

    /**
     * Method to notify a client about a client disconnection
     *
     * @param nickname of the disconnected client
     * @param threadNr of the disconnected client
     */
    public void playerDisconnected(String nickname, Integer threadNr) {
        Data data = new Data();
        data.setObjectType(Data.PLAYER_DISCONNECTED);
        data.setThreadNr(threadNr);
        data.setBody(nickname);
        sendData(data);
    }

    private void sendData(Data data) {
        try {
            sendObject(datagramSocket, data, connectionDetails);
        } catch (IOException e) {
            System.out.println("ClientHandler: error sending object");
        }
    }
}
