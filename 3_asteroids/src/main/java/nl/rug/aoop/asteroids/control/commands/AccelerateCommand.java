package nl.rug.aoop.asteroids.control.commands;

import nl.rug.aoop.asteroids.control.PlayerKeyListener;
import nl.rug.aoop.asteroids.model.gameobjects.Spaceship;
import nl.rug.aoop.asteroids.networking.client.Client;

/**
 * This class represents the Acceleration Command
 */
public class AccelerateCommand implements Command {
    /**
     * This method executes the command requested by the client.
     *
     * @param ship   ship that will execute the command.
     * @param client client that requested the command.
     */
    @Override
    public void execute(Spaceship ship, Client client) {
        ship.setAccelerateKeyPressed(true);
        if (client != null) client.setLastCommand(PlayerKeyListener.ACCELERATION_KEY, false);
    }

    /**
     * Will stop the execution of the command that was being executed.
     *
     * @param ship   ship that will stop executing the command.
     * @param client client that requested the stop.
     */
    @Override
    public void stopExecute(Spaceship ship, Client client) {
        ship.setAccelerateKeyPressed(false);
        if (client != null) client.setLastCommand(PlayerKeyListener.ACCELERATION_KEY, true);
    }
}
