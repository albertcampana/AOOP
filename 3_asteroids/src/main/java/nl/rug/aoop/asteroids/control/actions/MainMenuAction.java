package nl.rug.aoop.asteroids.control.actions;

import nl.rug.aoop.asteroids.view.ViewController;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * This action represents when a user wants to go to the MainMenu.
 */
public class MainMenuAction extends AbstractAction {
    /**
     * Construct a new main menu action. This calls the parent constructor to give the action a name.
     */
    public MainMenuAction() {
        super("MainMenu");
    }

    /**
     * Invoked when an action occurs.
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        ViewController viewController = ViewController.getInstance();
        viewController.goToMainMenu();
    }
}
