package nl.rug.aoop.asteroids.networking;

import nl.rug.aoop.asteroids.control.GameController;
import nl.rug.aoop.asteroids.networking.dataCommand.*;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents a Command Handler design pattern
 */
public class DataCommandHandler {

    /**
     * Map containing all the different commands.
     */
    private final Map<Integer, DataCommand> commandMap = new HashMap<>();

    private final GameController gameController = GameController.getInstance();

    /**
     * Class constructor
     */
    public DataCommandHandler() {

        commandMap.put(Data.PLAYER_CONNECTED, new PlayerConnectedDataCommand());
        commandMap.put(Data.PLAYER_DISCONNECTED, new PlayerDisconnectedDataCommand());
        commandMap.put(Data.START_GAME, new StartGameDataCommand());
        commandMap.put(Data.ABORT_GAME, new AbortGameDataCommand());
        commandMap.put(Data.SENDING_GAME_STATUS, new SendingGameDataCommand());
        commandMap.put(Data.GAME_STATUS_SENT, new GameSentDataCommand());
        commandMap.put(Data.BULLET, new BulletDataCommand());
        commandMap.put(Data.ASTEROID, new AsteroidDataCommand());
        commandMap.put(Data.SPACESHIP, new SpaceshipDataCommand());
    }

    /**
     * Execute a command
     *
     * @param command to be executed
     */
    public void executeCommands(Integer command, Data data) {
        if (commandMap.containsKey(command)) {
            commandMap.get(command).execute(gameController, data);
        }
    }
}
