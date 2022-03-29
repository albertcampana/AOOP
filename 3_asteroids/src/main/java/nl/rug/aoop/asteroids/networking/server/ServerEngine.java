package nl.rug.aoop.asteroids.networking.server;

import lombok.Getter;
import nl.rug.aoop.asteroids.control.CommandHandler;
import nl.rug.aoop.asteroids.control.GameController;
import nl.rug.aoop.asteroids.model.Game;
import nl.rug.aoop.asteroids.networking.CommandData;
import nl.rug.aoop.asteroids.networking.Data;
import nl.rug.aoop.asteroids.networking.PacketHandler;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class ServerEngine extends PacketHandler implements Runnable {

    @Getter
    private final int port;

    private boolean running = false;

    private final DatagramSocket datagramSocket;

    private final Game game = GameController.getGame();

    private final CommandHandler commandHandler = CommandHandler.getInstance();

    private final GameController gameController = GameController.getInstance();


    public ServerEngine() throws SocketException {

        datagramSocket = new DatagramSocket(0);

        port = datagramSocket.getLocalPort();
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
        running = true;
        while (running) {
            try {
                DatagramPacket datagramPacket = receive(datagramSocket);
                Data data = receiveData(datagramPacket);

                if (data.getObjectType() == Data.PLAYER_DISCONNECTED) {
                    Server.getClientHandlers().get(data.getThreadNr()).closeClientHandler();
                    Server.getClientHandlers().remove(data.getThreadNr());
                    gameController.removePlayer(data.getThreadNr());
                    Server.playerDisconnected((String) data.getBody(), data.getThreadNr());
                } else if (data.getObjectType() == Data.COMMAND) {
                    CommandData input = (CommandData) data.getBody();
                    executeCommand(input.getLastCommand(), input.getStop(), data.getThreadNr());
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        datagramSocket.close();
    }

    private void executeCommand(Integer lastCommand, Boolean pressed, int threadNr) {
        commandHandler.executeCommands(lastCommand, pressed, game.getSpaceshipMap().get(threadNr), null);
    }

    public void closeServerEngine() {
        running = false;
    }
}
