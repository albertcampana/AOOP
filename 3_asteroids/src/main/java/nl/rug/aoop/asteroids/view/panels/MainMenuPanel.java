package nl.rug.aoop.asteroids.view.panels;

import nl.rug.aoop.asteroids.control.GameController;
import nl.rug.aoop.asteroids.networking.client.Client;
import nl.rug.aoop.asteroids.networking.client.ClientMain;
import nl.rug.aoop.asteroids.networking.server.Server;
import nl.rug.aoop.asteroids.networking.server.ServerMain;
import nl.rug.aoop.asteroids.view.ViewController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * This class represents the Main Menu Panel.
 */
public class MainMenuPanel extends JPanel {

    private final ViewController viewController = ViewController.getInstance();
    private final JTextField userText = new JTextField();
    private JButton button;
    private JButton button1;
    private JButton button2;
    private JButton button3;
    private JButton button4;
    private final GameController gameController = GameController.getInstance();

    /**
     * Method that manages all the options available at the main menu
     */
    public MainMenuPanel() {
        JPanel userPanel = new JPanel();
        userPanel.setLayout(null);

        addLabel(userPanel);

        createButtons();
        setButtonBounds();
        addButtons(userPanel);
        addListeners();

        BoxLayout boxLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(boxLayout);

        add(userPanel);
    }

    private void addLabel(JPanel userPanel) {
        JLabel userLabel = new JLabel("Username");
        userLabel.setBounds(280, 50, 100, 30);
        userPanel.add(userLabel);

        userText.setBounds(370, 50, 150, 30);
        userPanel.add(userText);
    }

    private void addListeners() {
        button.addActionListener(this::actionPerformed3);

        button1.addActionListener(this::actionPerformed2);

        button2.addActionListener(this::actionPerformed);

        button3.addActionListener(this::actionPerformed5);

        button4.addActionListener(this::actionPerformed4);
    }

    private void addButtons(JPanel userPanel) {
        userPanel.add(button);
        userPanel.add(button1);
        userPanel.add(button2);
        userPanel.add(button3);
        userPanel.add(button4);
    }

    private void setButtonBounds() {
        button.setBounds(330, 120, 150, 30);
        button1.setBounds(330, 170, 150, 30);
        button2.setBounds(330, 220, 150, 30);
        button3.setBounds(330, 270, 150, 30);
        button4.setBounds(330, 320, 150, 30);
    }

    private void createButtons() {
        button = new JButton("Single Player");
        button1 = new JButton("Join Multiplayer");
        button2 = new JButton("Host Multiplayer");
        button3 = new JButton("Spectate Multiplayer");
        button4 = new JButton("High Scores");
    }

    private String getNickname() {
        String nickname = userText.getText();
        if (nickname.equals("")) return "Guest";
        return nickname;
    }

    private void actionPerformed(ActionEvent e) {
        Server server = ServerMain.startServer();
        gameController.setSpaceShipNickname(getNickname());
        gameController.addPlayer(getNickname(), 0);
        gameController.goToMultiplayerHall(null, server);
        viewController.goToMultiplayerHall(null);
    }

    private void actionPerformed2(ActionEvent e) {
        try {
            gameController.setSpaceShipNickname(getNickname());
            Client client = ClientMain.startClient(getNickname());
            viewController.addClientKeyListener(client);
            viewController.goToMultiplayerHall(client);
            gameController.goToMultiplayerHall(client, null);
        } catch (SocketException | UnknownHostException ex) {
            System.out.println("MainMenuPanel: Exception creating Client");
        }
    }

    private void actionPerformed5(ActionEvent e) {
        try {
            Client client = ClientMain.startClient(null);
            viewController.addClientKeyListener(client);
            viewController.goToMultiplayerHall(client);
            gameController.goToMultiplayerHall(client, null);
        } catch (SocketException | UnknownHostException ex) {
            System.out.println("MainMenuPanel: Exception creating Client");
        }
    }

    private void actionPerformed3(ActionEvent e) {
        gameController.setSpaceShipNickname(getNickname());
        viewController.newGame();
    }

    private void actionPerformed4(ActionEvent e) {
        viewController.goToHighScores();
    }
}
