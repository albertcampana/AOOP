package nl.rug.aoop.asteroids.control;

import lombok.Getter;
import nl.rug.aoop.asteroids.model.Game;
import nl.rug.aoop.asteroids.model.gameobjects.Asteroid;
import nl.rug.aoop.asteroids.model.gameobjects.Bullet;
import nl.rug.aoop.asteroids.model.gameobjects.Spaceship;
import nl.rug.aoop.asteroids.networking.client.Client;
import nl.rug.aoop.asteroids.networking.server.ClientHandler;
import nl.rug.aoop.asteroids.networking.server.Server;
import nl.rug.aoop.asteroids.util.Pair;
import nl.rug.aoop.asteroids.view.ViewController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Class that helps to control the different views
 */
public class GameController {

    /**
     * Game that is being played
     */
    @Getter
    private static final Game game = new Game();

    /**
     * Client of the game
     */
    private Client client = null;

    /**
     * Server of the game
     */
    private Server server = null;

    private static GameController instance;

    private final ViewController viewController = ViewController.getInstance();

    /**
     * Singleton pattern getInstance
     *
     * @return the instance of viewController
     */
    public static GameController getInstance() {
        if (instance == null) {
            new GameController();
        }
        return instance;
    }

    private GameController() {
        instance = this;
    }


    /**
     * Sets the multiplayer hall panel
     */
    public void goToMultiplayerHall(Client client, Server server) {
        this.client = client;
        this.server = server;
        if (client != null) {
            game.setScoreMap(new HashMap<>());
            game.setSpaceshipMap(new HashMap<>());
            resetGame();
        }
    }

    /**
     * Adds the new player to the multiplayer game on the multiplayer hall panel
     */
    public void addPlayer(String nickname, int threadNr) {
        ClientHandler.addNickname(threadNr, nickname);
        viewController.setPlayers(ClientHandler.getNicknames());
        if (threadNr != 0) addSpaceShip(nickname, threadNr);
    }

    /**
     * Removes the new player to the multiplayer game on the multiplayer hall panel
     */
    public void removePlayer(int threadNr) {
        ClientHandler.removeNickname(threadNr);
        viewController.setPlayers(ClientHandler.getNicknames());
        removeSpaceShip(threadNr);
    }

    /**
     * Adds a spaceship to both the Collection and the Map
     *
     * @param nickname nickname of the user owner of the ship
     * @param threadNr thread number of the client
     */
    private void addSpaceShip(String nickname, int threadNr) {
        Spaceship spaceship = new Spaceship(nickname);
        spaceship.setIdentifier(threadNr);
        game.getSpaceshipCollection().add(spaceship);
        game.getSpaceshipMap().put(threadNr, spaceship);
    }

    /**
     * Removes the spaceship from the collection
     *
     * @param threadNr thread number associated to the spaceship
     */
    private void removeSpaceShip(int threadNr) {
        game.getSpaceshipCollection().remove(game.getSpaceshipMap().remove(threadNr));
    }

    /**
     * Sets a nickname to the ship
     *
     * @param nickname of the player
     */
    public void setSpaceShipNickname(String nickname) {
        game.getSpaceShip().setNickname(nickname);
    }

    /**
     * Method used to close the game even if it has not finished yet.
     */
    public void abortGame() {
        if (client != null) client.closeClient();
        else if (server != null) server.closeServer();
        System.out.println(server);
        client = null;
        server = null;
        viewController.goToMainMenu();
    }

    /**
     * Method to start the Game. If the game is multiplayer it will start on all clients.
     */
    public void startGame() {
        viewController.newGame();
        if (server != null) server.startGame();
    }

    /**
     * Method to call newGame() from the ViewController class
     */
    public void newGame() {
        viewController.newGame();
    }

    /**
     * Adds the players to the Multiplayer panel
     *
     * @param nicknames Map containing the id and nicknames of all players
     */
    public void setPlayers(Map<Integer, String> nicknames) {
        viewController.setPlayers(nicknames);
    }

    /**
     * Sets the main menu panel
     */
    public void goToMainMenu() {
        viewController.goToMainMenu();
    }

    /**
     * Method that resets all the settings to the original state.
     */
    public void resetGame() {
        game.setClient(true);
        game.setBullets(new ArrayList<>());
        game.setAsteroids(new ArrayList<>());
        game.setSpaceshipCollection(new ArrayList<>());
    }

    /**
     * Method that adds the bullets to the array.
     *
     * @param bullet the bullet to be added.
     */
    public void addBullet(Bullet bullet) {
        game.getBullets().add(bullet);
    }

    /**
     * Method that adds the asteroid to the array.
     *
     * @param asteroid the asteroid to be added.
     */
    public void addAsteroid(Asteroid asteroid) {
        game.getAsteroids().add(asteroid);
    }

    /**
     * Method that adds the spaceship to the Collection and Map.
     *
     * @param spaceship the spaceship to be added.
     * @param threadNr  the thread number associated to the spaceship
     */
    public void addSpaceship(Spaceship spaceship, int threadNr) {
        game.getSpaceshipCollection().add(spaceship);
        game.getSpaceshipMap().put(threadNr, spaceship);
        game.getScoreMap().put(threadNr, new Pair<>(spaceship.getNickname(), spaceship.getScore()));
    }

    /**
     * Method that prints the updated game.
     */
    public void gameUpdated() {
        viewController.gameUpdated();
    }
}

