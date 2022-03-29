package nl.rug.aoop.asteroids.networking.client;

import lombok.Getter;
import nl.rug.aoop.asteroids.networking.Data;
import nl.rug.aoop.asteroids.networking.DataCommandHandler;
import nl.rug.aoop.asteroids.networking.PacketHandler;
import nl.rug.aoop.asteroids.view.ViewController;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.HashMap;
import java.util.Map;

/**
 * Class that represents the Client Updater
 */
public class ClientUpdater extends PacketHandler implements Runnable {

    private final DatagramSocket datagramSocket;

    private static boolean running;

    @Getter
    private static final Map<Integer, String> nicknames = new HashMap<>();

    private final DataCommandHandler dataCommandHandler;

    public ClientUpdater(DatagramSocket datagramSocket, int port) {
        ViewController viewController = ViewController.getInstance();
        viewController.setPort(port);
        this.datagramSocket = datagramSocket;
        running = false;
        dataCommandHandler = new DataCommandHandler();
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
                Data input = receiveData(datagramPacket);

                dataCommandHandler.executeCommands(input.getObjectType(), input);

            } catch (IOException e) {
                System.out.println("ClientUpdater closed");
            }
        }
    }

    /**
     * Running set to false so it closes on the while.
     */
    public static void closeClientUpdater() {
        running = false;
    }
}
