package nl.rug.aoop.asteroids.networking.dataCommand;

import nl.rug.aoop.asteroids.control.GameController;
import nl.rug.aoop.asteroids.model.gameobjects.Asteroid;
import nl.rug.aoop.asteroids.networking.Data;

/**
 * Actions to execute when receiving an asteroid packet
 */
public class AsteroidDataCommand implements DataCommand {
    /**
     * Actions to execute when receiving a Data packet
     *
     * @param gameController that will execute the order
     * @param data           packet received
     */
    @Override
    public void execute(GameController gameController, Data data) {
        gameController.addAsteroid((Asteroid) data.getBody());
    }
}
