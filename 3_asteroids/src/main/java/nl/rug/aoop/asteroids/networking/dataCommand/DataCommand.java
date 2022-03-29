package nl.rug.aoop.asteroids.networking.dataCommand;

import nl.rug.aoop.asteroids.control.GameController;
import nl.rug.aoop.asteroids.networking.Data;

/**
 * Interface that handles packet receiving
 */
public interface DataCommand {
    /**
     * Actions to execute when receiving a Data packet
     *
     * @param gameController that will execute the order
     * @param data           packet received
     */
    void execute(GameController gameController, Data data);
}
