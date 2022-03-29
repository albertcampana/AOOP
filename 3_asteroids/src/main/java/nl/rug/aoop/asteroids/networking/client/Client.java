package nl.rug.aoop.asteroids.networking.client;

import lombok.Getter;
import nl.rug.aoop.asteroids.networking.CommandData;
import nl.rug.aoop.asteroids.networking.Data;
import nl.rug.aoop.asteroids.networking.PacketHandler;

import java.io.IOException;
import java.net.*;

/**
 * Client class
 */
public class Client extends PacketHandler implements Runnable {

    private final DatagramSocket datagramSocket;
    private InetSocketAddress connectionDetails;
    private boolean running;
    private final int port;
    private final String nickname;

    @Getter
    private int threadNr;

    private CommandData lastCommand;

    private final InetAddress ip;

    /**
     * Client Constructor
     *
     * @param port     port of the server
     * @param nickname player nickname
     * @throws SocketException Socket Exception
     */
    public Client(int port, String nickname, InetAddress ip) throws SocketException {
        this.port = port;
        this.ip = ip;
        this.nickname = nickname;
        this.running = false;
        datagramSocket = new DatagramSocket(0);
    }

    private void initialiseConnection() throws IOException {
        sendInitialPacket();

        DatagramPacket datagramPacket = receive(datagramSocket);
        Data data = receiveData(datagramPacket);
        Integer serverEnginePort = (Integer) data.getBody();

        connectionDetails = new InetSocketAddress(datagramPacket.getAddress(), serverEnginePort);

        this.threadNr = data.getThreadNr();

        ClientUpdater clientUpdater = new ClientUpdater(datagramSocket, port);
        Thread thread = new Thread(clientUpdater);
        thread.start();
    }

    private void sendInitialPacket() {
        connectionDetails = new InetSocketAddress(ip, port);
        Data initialData = new Data();
        if (nickname == null) initialData.setObjectType(Data.SPECTATE);
        else initialData.setObjectType(Data.PLAYER_CONNECTED);
        initialData.setBody(nickname);
        sendData(initialData);
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
        try {
            initialiseConnection();
            running = true;

            Data data = new Data();
            data.setObjectType(Data.COMMAND);
            data.setThreadNr(threadNr);

            while (running) {
                sendLastCommand(data);
            }

        } catch (IOException | InterruptedException e) {
            System.out.println("Client: error while running");
        }
    }

    private void sendLastCommand(Data data) throws InterruptedException {
        if (lastCommand != null) {
            data.setBody(lastCommand);
            sendData(data);
            lastCommand = null;
        }
        Thread.sleep(10);
    }

    /**
     * Set last command in order to send it to the server
     *
     * @param command last command executed by the player
     */
    public void setLastCommand(Integer command, Boolean stop) {
        lastCommand = new CommandData(command, stop);
    }

    /**
     * Closes the client
     */
    public void closeClient() {
        this.running = false;

        Data data = new Data();
        data.setObjectType(Data.PLAYER_DISCONNECTED);
        data.setBody(nickname);

        sendData(data);

        ClientUpdater.closeClientUpdater();
        datagramSocket.close();
    }

    private void sendData(Data data) {
        try {
            data.setThreadNr(threadNr);
            sendObject(datagramSocket, data, connectionDetails);
        } catch (IOException e) {
            System.out.println("Client: error sending data");
        }
    }
}
