package nl.rug.aoop.asteroids.view;

import nl.rug.aoop.asteroids.control.actions.MainMenuAction;
import nl.rug.aoop.asteroids.control.actions.QuitAction;

import javax.swing.*;
import java.awt.*;

/**
 * The main window that's used for displaying the game.
 */
public class AsteroidsFrame extends JFrame {
    /**
     * The title which appears in the upper border of the window.
     */
    private static final String WINDOW_TITLE = "Asteroids";

    /**
     * The size that the window should be.
     */
    public static final Dimension WINDOW_SIZE = new Dimension(800, 800);

    private JPanel lastPanel;

    /**
     * Constructs the game's main window.
     */
    public AsteroidsFrame() {
        initSwingUI();
    }

    /**
     * A helper method to do the tedious task of initializing the Swing UI components.
     */
    private void initSwingUI() {
        setTitle(WINDOW_TITLE);
        setSize(WINDOW_SIZE);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Game");
        menuBar.add(menu);
        menu.add(new QuitAction());
        menu.add(new MainMenuAction());

        setJMenuBar(menuBar);
    }

    /**
     * Changes panel in the frame and shows panel given as a parameter
     *
     * @param panel JPanel that has to be shown.
     */
    public void addPanel(JPanel panel) {
        if (lastPanel != null) getContentPane().remove(lastPanel);
        revalidate();
        lastPanel = panel;
        add(panel);
        this.setVisible(true);
    }
}
