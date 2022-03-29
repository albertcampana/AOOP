package nl.rug.aoop.asteroids.networking;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Data class to send data over udp
 */
@Getter
@Setter
public class Data implements Serializable {

    /**
     * Identifier for player connected packets
     */
    public static int PLAYER_CONNECTED = 1;

    /**
     * Identifier for player diconnected packets
     */
    public static int PLAYER_DISCONNECTED = 2;

    /**
     * Identifier for start game packets
     */
    public static int START_GAME = 3;

    /**
     * Identifier for abort game packets
     */
    public static int ABORT_GAME = 4;

    /**
     * Identifier for command packets
     */
    public static int COMMAND = 5;

    /**
     * Identifier for sending game status packets
     */
    public static int SENDING_GAME_STATUS = 6;

    /**
     * Identifier for game status sent packets
     */
    public static int GAME_STATUS_SENT = 7;

    /**
     * Identifier for bullet packets
     */
    public static int BULLET = 8;

    /**
     * Identifier for asteroid packets
     */
    public static int ASTEROID = 9;

    /**
     * Identifier for spaceship packets
     */
    public static int SPACESHIP = 10;

    /**
     * Identifier for string packets
     */
    public static int STRING = 11;

    /**
     * Identifier for int packets
     */
    public static int INT = 12;

    /**
     * Identifier for int packets
     */
    public static int SPECTATE = 13;

    private int objectType;

    private int threadNr;

    private Object body;

    /**
     * Class constructor
     */
    public Data() {
    }
}
