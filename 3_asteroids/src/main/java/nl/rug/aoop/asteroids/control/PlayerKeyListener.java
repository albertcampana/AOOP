package nl.rug.aoop.asteroids.control;

import nl.rug.aoop.asteroids.model.gameobjects.Spaceship;
import nl.rug.aoop.asteroids.networking.client.Client;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * This class is responsible for handling keyboard input for a single player that is bound to a ship.
 */
public class PlayerKeyListener implements KeyListener {
    /**
     * The key that, when pressed, causes the ship to accelerate.
     */
    public static final int ACCELERATION_KEY = KeyEvent.VK_W;

    /**
     * The key that turns the ship left, or counter-clockwise.
     */
    public static final int LEFT_KEY = KeyEvent.VK_A;

    /**
     * The key that turns the ship right, or clockwise.
     */
    public static final int RIGHT_KEY = KeyEvent.VK_D;

    /**
     * The key that causes the ship to fire its weapon.
     */
    public static final int FIRE_WEAPON_KEY = KeyEvent.VK_SPACE;

    /**
     * Instance of the Command Handler
     */
    private final CommandHandler commandHandler;

    /**
     * Represents the spaceship
     */
    private final Spaceship ship;

    /**
     * Client of the game
     */
    private final Client client;

    /**
     * Constructs a new player key listener to control the given ship.
     *
     * @param ship The ship that this key listener will control.
     */
    public PlayerKeyListener(Spaceship ship, Client client) {
        this.ship = ship;
        this.client = client;
        commandHandler = CommandHandler.getInstance();
    }

    /**
     * This method is invoked when a key is pressed and sets the corresponding fields in the spaceship to true.
     *
     * @param event Key event that triggered the method.
     */
    @Override
    public void keyPressed(KeyEvent event) {
        commandHandler.executeCommands(event.getKeyCode(), false, ship, client);
    }

    /**
     * This method is invoked when a key is released and sets the corresponding fields in the spaceship to false.
     *
     * @param event Key event that triggered the method.
     */
    @Override
    public void keyReleased(KeyEvent event) {
        commandHandler.executeCommands(event.getKeyCode(), true, ship, client);
    }

    /**
     * This method doesn't do anything, but we must provide an empty implementation to satisfy the contract of the
     * KeyListener interface.
     *
     * @param event Key event that triggered the method.
     */
    @Override
    public void keyTyped(KeyEvent event) {
    }
}
