package nl.rug.aoop.asteroids.networking.dataCommand;

import nl.rug.aoop.asteroids.control.GameController;
import nl.rug.aoop.asteroids.model.gameobjects.Bullet;
import nl.rug.aoop.asteroids.networking.Data;

/**
 * Actions to execute when receiving a bullet packet
 */
public class BulletDataCommand implements DataCommand {

    /**
     * Actions to execute when receiving a Bullet packet
     *
     * @param gameController that will execute the order
     * @param data           packet received
     */
    @Override
    public void execute(GameController gameController, Data data) {
        gameController.addBullet((Bullet) data.getBody());
    }
}
