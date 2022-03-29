package nl.rug.aoop.asteroids.view.panels;

import nl.rug.aoop.asteroids.control.GameController;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

/**
 * Class that represents the Multiplayer Panel. It is the panel right before starting the game.
 * All the connected players wait here until the game starts.
 */
public class MultiplayerPanel extends JPanel {

    private int port;
    private JLabel title;
    private JButton startButton;
    private JLabel ipExplain3;
    private final JList<String> players = new JList<>();
    private final DefaultListModel<String> listModel = new DefaultListModel<>();
    private final GameController gameController = GameController.getInstance();
    private String address = "0.0.0.0";

    /**
     * Constructor for the MultiplayerPanel
     */
    public MultiplayerPanel() {
        setLayout(null);

        addTitle();
        addLabel();
        addInfo();
        addInfo2();
        addPlayers();
        addStartButton();
        addAbortButton();

        setVisible(true);
    }

    private void addAbortButton() {
        JButton abortGame = new JButton("Abort Game");
        abortGame.setBounds(400, 600, 120, 40);
        abortGame.addActionListener(e -> gameController.abortGame());
        add(abortGame);
    }

    private void addStartButton() {
        startButton = new JButton("Start Game");
        startButton.setBounds(260, 600, 120, 40);
        startButton.addActionListener(e -> gameController.startGame());
        add(startButton);
    }

    private void addPlayers() {
        players.setBounds(80, 180, 250, 300);
        players.setBackground(new Color(60, 63, 65));
        players.setFont(new Font("Verdana", Font.PLAIN, 18));
        add(players);
    }

    private void addInfo() {
        JLabel ipExplain = new JLabel("In order to obtain the ip: Open cmd and type ipconfig.");
        ipExplain.setBounds(250, 70, 300, 40);
        add(ipExplain);
        JLabel ipExplain2 = new JLabel("Look for your wifi ipv4 address.");
        ipExplain2.setBounds(310, 90, 250, 40);
        add(ipExplain2);
    }

    private void addInfo2() {
        ipExplain3 = new JLabel("To play locally. Insert: " + address);
        ipExplain3.setBounds(303, 110, 300, 40);
        add(ipExplain3);
    }

    private void addLabel() {
        JLabel text = new JLabel("Connected Users:");
        text.setBounds(30, 120, 250, 70);
        text.setFont(new Font("Verdana", Font.PLAIN, 18));
        add(text);
    }

    private void addTitle() {
        title = new JLabel("Port: " + this.port);
        title.setBounds(320, 20, 200, 70);
        title.setFont(new Font("Verdana", Font.PLAIN, 24));
        add(title);
    }

    /**
     * Sets the port
     *
     * @param port port to be connected
     */
    public void setPort(int port) {
        this.port = port;
        title.setText("Port: " + this.port);
    }

    /**
     * Sets the ip address
     *
     * @param address port to be connected
     */
    public void setIP(String address) {
        this.address = address;
        ipExplain3.setText("To play locally. Insert: " + address);
    }

    /**
     * Puts the players inside the list
     *
     * @param nicknames all the players nicknames.
     */
    public void setPlayers(Map<Integer, String> nicknames) {
        listModel.removeAllElements();
        for (var entry : nicknames.entrySet()) {
            listModel.addElement(entry.getValue());
        }
        players.setModel(listModel);
    }

    public void deactivateStart() {
        startButton.setEnabled(false);
    }
}
