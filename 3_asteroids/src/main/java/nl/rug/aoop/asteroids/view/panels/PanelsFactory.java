package nl.rug.aoop.asteroids.view.panels;

import nl.rug.aoop.asteroids.model.Game;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents the Factory design pattern for the different panels of the view.
 */
public class PanelsFactory {

    private final Map<String, JPanel> panelsMap;

    /**
     * Constant for the Main Menu panel
     */
    public static final String MAIN_MENU = "MAIN_MENU";
    /**
     * Constant for the High Score panel
     */
    public static final String HIGH_SCORE = "HIGH_SCORE";
    /**
     * Constant for the game panel
     */
    public static final String GAME = "GAME";
    /**
     * Constant for the Multiplayer hall panel.
     */
    public static final String MULTIPLAYER_HALL = "MULTIPLAYER_HALL";

    /**
     * Constructor of the Panel Factory.
     *
     * @param game the actual game.
     */
    public PanelsFactory(Game game) {
        panelsMap = new HashMap<>();
        panelsMap.put(MAIN_MENU, new MainMenuPanel());
        panelsMap.put(HIGH_SCORE, new HighScorePanel());
        panelsMap.put(GAME, new AsteroidsPanel(game));
        panelsMap.put(MULTIPLAYER_HALL, new MultiplayerPanel());
    }

    /**
     * Method that returns the panel that will be added to the Main Frame.
     *
     * @param string name of the requested panel
     * @return the JPanel that will be added to the Main Frame.
     */
    public JPanel getPanel(String string) {
        return panelsMap.getOrDefault(string.toUpperCase(), null);
    }
}
