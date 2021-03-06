package nl.rug.aoop.asteroids.model;

import lombok.Getter;
import lombok.Setter;
import nl.rug.aoop.asteroids.control.GameUpdater;
import nl.rug.aoop.asteroids.gameobserver.ObservableGame;
import nl.rug.aoop.asteroids.model.gameobjects.Asteroid;
import nl.rug.aoop.asteroids.model.gameobjects.Bullet;
import nl.rug.aoop.asteroids.model.gameobjects.Spaceship;
import nl.rug.aoop.asteroids.util.Pair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is the main model for the Asteroids game. It contains all game objects, and has methods to start and stop
 * the game.
 * <p>
 * This is strictly a model class, containing only the state of the game. Updates to the game are done in
 * {@link GameUpdater}, which runs in its own thread, and manages the main game loop and physics updates.
 */
public class Game extends ObservableGame {

    /**
     * The spaceship object that the player is in control of.
     */
    @Getter
    private final Spaceship spaceShip;

    /**
     * Collection containing all the spaceships.
     */
    @Getter
    @Setter
    private Collection<Spaceship> spaceshipCollection;

    /**
     * Map containing all the spaceships with it's corresponding thread number.
     */
    @Getter
    @Setter
    private Map<Integer, Spaceship> spaceshipMap;

    /**
     * Map containing all the scores of each player.
     */
    @Getter
    @Setter
    private Map<Integer, Pair<String, Integer>> scoreMap = new HashMap<>();

    /**
     * The list of all bullets currently active in the game.
     */
    @Getter
    @Setter
    private Collection<Bullet> bullets;

    /**
     * The list of all asteroids in the game.
     */
    @Getter
    @Setter
    private Collection<Asteroid> asteroids;

    /**
     * Boolean that states if it's a client or not.
     */
    @Setter
    @Getter
    private boolean isClient = false;

    /**
     * Indicates whether the game is running. Setting this to false causes the game to exit its loop and quit.
     */
    private volatile boolean running = false;

    /**
     * The game updater thread, which is responsible for updating the game's state as time goes on.
     */
    private Thread gameUpdaterThread;

    /**
     * Number of milliseconds to wait for the game updater to exit its game loop.
     */
    private static final int EXIT_TIMEOUT_MILLIS = 100;


    /**
     * Constructs a new game, with a new spaceship and all other model data in its default starting state.
     */
    public Game() {
        spaceShip = new Spaceship();
        spaceshipCollection = new ArrayList<>();
        spaceshipMap = new HashMap<>();

        spaceshipCollection.add(spaceShip);
        spaceshipMap.put(0, spaceShip);
        initializeGameData();
    }

    /**
     * Initializes all the model objects used by the game. Can also be used to reset the game's state back to a
     * default starting state before beginning a new game.
     */
    public void initializeGameData() {
        bullets = new ArrayList<>();
        asteroids = new ArrayList<>();
        spaceshipCollection.forEach(Spaceship::reset);
    }

    /**
     * @return Whether the game is running.
     */
    public synchronized boolean isRunning() {
        return running;
    }

    /**
     * @return True if the player's ship has been destroyed, or false otherwise.
     */
    public boolean isGameOver() {
        return spaceShip.isDestroyed();
    }

    /**
     * Using this game's current model, spools up a new game updater thread to begin a game loop and start processing
     * user input and physics updates. Only if the game isn't currently running, that is.
     */
    public void start() {
        if (!running) {
            running = true;
            gameUpdaterThread = new Thread(new GameUpdater(this));
            gameUpdaterThread.start();
        }
    }

    /**
     * Tries to quit the game, if it is running.
     */
    public void quit() {
        if (running) {
            try {
                // Attempt to wait for the game updater to exit its game loop.
                gameUpdaterThread.join(EXIT_TIMEOUT_MILLIS);
            } catch (InterruptedException exception) {
                System.err.println("Interrupted while waiting for the game updater thread to finish execution.");
            } finally {
                running = false;
                // Throw away the game updater thread and let the GC remove it.
                gameUpdaterThread = null;
            }
        }
    }
}
