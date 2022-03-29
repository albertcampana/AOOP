package nl.rug.aoop.asteroids.control;

import nl.rug.aoop.asteroids.control.commands.*;
import nl.rug.aoop.asteroids.model.gameobjects.Spaceship;
import nl.rug.aoop.asteroids.networking.client.Client;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents a Command Handler design pattern
 */
public class CommandHandler {

    /**
     * Instance of a Command Handler
     */
    private static CommandHandler instance;

    /**
     * Map containing all the different commands.
     */
    private final Map<Integer, Command> commandMap = new HashMap<>();

    private CommandHandler() {
        instance = this;
        commandMap.put(PlayerKeyListener.ACCELERATION_KEY, new AccelerateCommand());
        commandMap.put(PlayerKeyListener.LEFT_KEY, new TurnLeftCommand());
        commandMap.put(PlayerKeyListener.RIGHT_KEY, new TurnRightCommand());
        commandMap.put(PlayerKeyListener.FIRE_WEAPON_KEY, new FireCommand());
    }

    /**
     * Method that returns an instance of the singleton Command Handler.
     *
     * @return the instance of the Command Handler.
     */
    public static CommandHandler getInstance() {
        if (instance == null) {
            new CommandHandler();
        }
        return instance;
    }


    /**
     * Execute a command and returns a response
     *
     * @param command to be executed
     */
    public synchronized void executeCommands(Integer command, Boolean stop, Spaceship ship, Client client) {
        if (ship != null && commandMap.containsKey(command)) {
            if (stop) commandMap.get(command).stopExecute(ship, client);
            else commandMap.get(command).execute(ship, client);
        }
    }
}
