package nl.rug.aoop.asteroids.view;

import nl.rug.aoop.asteroids.control.actions.MainMenuAction;
import nl.rug.aoop.asteroids.control.actions.QuitAction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Class for the Main Frame, it extends JFrame and implements ActionListener.
 */
public class MainFrame extends JFrame implements ActionListener {

    private final ViewController viewController;

    /**
     * The size that the window should be.
     */
    public static final Dimension WINDOW_SIZE = new Dimension(800, 800);

    private static final String WINDOW_TITLE = "Asteroids";

    private JPanel lastPanel;

    /**
     * Constructor for the Main Frame
     */
    public MainFrame() {
        viewController = ViewController.getInstance();
        initFrame();
    }

    private void initFrame() {
        setTitle(WINDOW_TITLE);
        setSize(WINDOW_SIZE);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Game");
        menuBar.add(menu);
        menu.add(new QuitAction());
        menu.add(new MainMenuAction());
        setJMenuBar(menuBar);
    }

    /**
     * Method that starts a new game
     *
     * @param e action on the event
     */
    public void actionPerformed(ActionEvent e) {
        viewController.newGame();
    }

    /**
     * Method that adds a JPanel to the Main Frame.
     *
     * @param panel panel that will be added to the MainFrame.
     */
    public void addPanel(JPanel panel) {
        if (lastPanel != null) getContentPane().remove(lastPanel);
        repaint();
        revalidate();
        lastPanel = panel;
        add(panel);
        repaint();
        setVisible(true);
    }
}