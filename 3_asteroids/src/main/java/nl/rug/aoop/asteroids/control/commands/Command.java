package nl.rug.aoop.asteroids.control.commands;

import nl.rug.aoop.asteroids.model.gameobjects.Spaceship;
import nl.rug.aoop.asteroids.networking.client.Client;

/**
 * Represents a command
 */
public interface Command {
    void execute(Spaceship ship, Client client);

    void stopExecute(Spaceship ship, Client client);
}
