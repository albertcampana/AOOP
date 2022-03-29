package nl.rug.aoop.asteroids.networking.dataCommand;

import nl.rug.aoop.asteroids.control.GameController;
import nl.rug.aoop.asteroids.networking.Data;
import nl.rug.aoop.asteroids.networking.client.ClientUpdater;

/**
 * Actions to execute when receiving a player disconnected packet
 */
public class PlayerDisconnectedDataCommand implements DataCommand {
    /**
     * Actions to execute when receiving a Data packet
     *
     * @param gameController that will execute the order
     * @param data           packet received
     */
    @Override
    public void execute(GameController gameController, Data data) {
        ClientUpdater.getNicknames().remove(data.getThreadNr(), (String) data.getBody());
        gameController.removePlayer(data.getThreadNr());
    }
}
