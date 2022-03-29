package nl.rug.aoop.asteroids.networking;

import org.apache.commons.lang3.SerializationUtils;

import java.io.IOException;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

/**
 * Abstract class that handles the packets that are sent through the network
 */
public abstract class PacketHandler {

    private static final int MAX_SIZE = 1024;

    /**
     * Method that sends objects over udp connection
     *
     * @param datagramSocket    socket to send object to
     * @param object            object that we want to send
     * @param connectionDetails connections details of where to send the object
     * @throws IOException throws exception if send does not work
     */
    public void sendObject(DatagramSocket datagramSocket, Object object, InetSocketAddress connectionDetails) throws IOException {
        byte[] data = SerializationUtils.serialize((Serializable) object);
        DatagramPacket datagramPacket = new DatagramPacket(data, data.length, connectionDetails.getAddress(), connectionDetails.getPort());
        datagramSocket.send(datagramPacket);
    }

    /**
     * Receive a Datagram packet of a socket
     *
     * @param datagramSocket socket from where to read the packet
     * @return the first datagram packet of the socket
     * @throws IOException throws exception if receive does not work
     */
    public DatagramPacket receive(DatagramSocket datagramSocket) throws IOException {
        byte[] data = new byte[MAX_SIZE];

        DatagramPacket receivePacket = new DatagramPacket(data, data.length);
        datagramSocket.receive(receivePacket);


        return receivePacket;
    }

    /**
     * Get a Data object from a datagram packet
     *
     * @param datagramPacket that we want to convert into Data object
     * @return Data contained in the packet
     */
    public Data receiveData(DatagramPacket datagramPacket) {
        return SerializationUtils.deserialize(datagramPacket.getData());
    }
}
