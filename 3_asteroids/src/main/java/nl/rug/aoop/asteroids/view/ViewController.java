package nl.rug.aoop.asteroids.view;

import lombok.Getter;
import nl.rug.aoop.asteroids.control.GameController;
import nl.rug.aoop.asteroids.control.GameUpdater;
import nl.rug.aoop.asteroids.control.PlayerKeyListener;
import nl.rug.aoop.asteroids.control.actions.NewGameAction;
import nl.rug.aoop.asteroids.model.Game;
import nl.rug.aoop.asteroids.networking.client.Client;
import nl.rug.aoop.asteroids.view.panels.AsteroidsPanel;
import nl.rug.aoop.asteroids.view.panels.MultiplayerPanel;
import nl.rug.aoop.asteroids.view.panels.PanelsFactory;

import java.awt.event.ActionEvent;
import java.util.Map;

/**
 * Class that helps to control the different views
 */
public class ViewController {

    /**
     * Instance of the View Controller
     */
    private static ViewController instance;

    /**
     * Frame that is in charge of displaying the game
     */
    private final AsteroidsFrame gameFrame;

    /**
     * Main frame of the asteroids game
     */
    private final MainFrame mainFrame;

    /**
     * Game that is being played
     */
    @Getter
    private final Game game = GameController.getGame();

    /**
     * Used to swap between the different panels.
     */
    private final PanelsFactory panelsFactory;

    private long timeSinceLastTick = 0;
    private long previousTime = System.currentTimeMillis();

    /**
     * Singleton pattern getInstance
     *
     * @return the instance of viewController
     */
    public static ViewController getInstance() {
        if (instance == null) {
            new ViewController();
        }
        return instance;
    }

    private ViewController() {
        instance = this;

        //Create frames
        gameFrame = new AsteroidsFrame();
        mainFrame = new MainFrame();

        panelsFactory = new PanelsFactory(game);

        goToMainMenu();
    }

    /**
     * Sets the high score panel
     */
    public void goToHighScores() {
        mainFrame.addPanel(panelsFactory.getPanel(PanelsFactory.HIGH_SCORE));
    }

    /**
     * Sets the main menu panel
     */
    public void goToMainMenu() {
        mainFrame.addPanel(panelsFactory.getPanel(PanelsFactory.MAIN_MENU));
    }

    /**
     * Sets the multiplayer hall panel
     */
    public void goToMultiplayerHall(Client client) {
        MultiplayerPanel panel = (MultiplayerPanel) panelsFactory.getPanel(PanelsFactory.MULTIPLAYER_HALL);
        mainFrame.addPanel(panel);
        if (client != null) {
            panel.deactivateStart();
        }
    }

    /**
     * Prints the port of the multiplayer game on the multiplayer hall panel
     *
     * @param port of the multiplayer game
     */
    public void setPort(int port) {
        MultiplayerPanel panel = (MultiplayerPanel) panelsFactory.getPanel(PanelsFactory.MULTIPLAYER_HALL);
        panel.setPort(port);
    }

    /**
     * Adds the players to the Multiplayer panel
     *
     * @param nicknames Map containing the id and nicknames of all players
     */
    public void setPlayers(Map<Integer, String> nicknames) {
        MultiplayerPanel panel = (MultiplayerPanel) panelsFactory.getPanel(PanelsFactory.MULTIPLAYER_HALL);
        panel.setPlayers(nicknames);
    }

    /**
     * Displays single game on screen.
     */
    public void newGame() {
        gameFrame.addPanel(panelsFactory.getPanel(PanelsFactory.GAME));
        mainFrame.setVisible(false);
        gameFrame.addKeyListener(new PlayerKeyListener(game.getSpaceShip(), null));

        // Generate a new action event so that we can use the NewGameAction to start a new game.
        new NewGameAction(game).actionPerformed(
                // Just use a dummy action; NewGameAction doesn't care about the action event's properties.
                new ActionEvent(gameFrame, ActionEvent.ACTION_PERFORMED, null)
        );
    }

    /**
     * Adds client key listener to the game frame
     *
     * @param client that has to listen
     */
    public void addClientKeyListener(Client client) {
        gameFrame.addKeyListener(new PlayerKeyListener(game.getSpaceShip(), client));
    }

    /**
     * Method that updates the game.
     */
    public void gameUpdated() {
        AsteroidsPanel panel = (AsteroidsPanel) panelsFactory.getPanel(PanelsFactory.GAME);

        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - previousTime;
        timeSinceLastTick += elapsedTime;

        if (timeSinceLastTick >= GameUpdater.MILLISECONDS_PER_TICK) {
            panel.onGameUpdated(timeSinceLastTick);
            timeSinceLastTick = 0L;
        }

        previousTime = currentTime;
    }

    public void setIP(String ip) {
        MultiplayerPanel panel = (MultiplayerPanel) panelsFactory.getPanel(PanelsFactory.MULTIPLAYER_HALL);
        panel.setIP(ip);
    }
}

