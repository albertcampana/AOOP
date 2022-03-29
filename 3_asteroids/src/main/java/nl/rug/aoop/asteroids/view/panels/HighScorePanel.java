package nl.rug.aoop.asteroids.view.panels;

// Packages to import

import com.formdev.flatlaf.FlatDarculaLaf;
import nl.rug.aoop.asteroids.database.DatabaseManager;
import nl.rug.aoop.asteroids.database.Score;
import nl.rug.aoop.asteroids.view.ViewController;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;
import java.util.TreeSet;

/**
 * Class that represents the panel of the high scores.
 */
public class HighScorePanel extends JPanel {
    /**
     * Table containing the data.
     */
    JTable table;

    /**
     * Instance of the view controller.
     */
    private final ViewController viewController;

    /**
     * Pane to be able to scroll up or down the scores.
     */
    JScrollPane jScrollPane;

    /**
     * Array of Arrays containing the data of each player.
     */
    String[][] data;

    /**
     * Names of the columns of the tables.
     */
    String[] columnNames = {"User", "Score"};

    /**
     * Constructor of the High Score panel.
     */
    public HighScorePanel() {
        FlatDarculaLaf.setup();
        initTableSettings();
        viewController = ViewController.getInstance();

        String[] optionsToChoose = {"Single Player", "Multiplayer"};
        addButton();
        addJComboBox(optionsToChoose);
        addScrollPane();

        setSize(450, 500);
    }

    private void addScrollPane() {
        table = new JTable(data, columnNames);
        table.setBounds(30, 200, 200, 300);
        jScrollPane = new JScrollPane(table);
        add(jScrollPane);
    }

    private void addJComboBox(String[] optionsToChoose) {
        JComboBox<String> jComboBox = new JComboBox<>(optionsToChoose);
        getSinglePlayerDB();
        jComboBox.addActionListener(e -> {
            String gameMode = jComboBox.getItemAt(jComboBox.getSelectedIndex());
            if (Objects.equals(gameMode, "Single Player")) {
                getSinglePlayerDB();
            } else {
                getMultiPlayerDB();
            }
            updateView();
        });
        add(jComboBox);
    }

    private void addButton() {
        JButton button = new JButton("Main Menu");
        button.setAlignmentX(CENTER_ALIGNMENT);
        add(button);
        button.addActionListener(e -> viewController.goToMainMenu());
    }

    private void initTableSettings() {
        setBackground(new Color(69, 73, 74));
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
    }

    private void updateView() {
        remove(jScrollPane);
        repaint();
        revalidate();
        addScrollPane();
        repaint();
        revalidate();
    }

    private void getSinglePlayerDB() {
        DatabaseManager databaseManager = new DatabaseManager("test");
        TreeSet<Score> treeSet = databaseManager.getGameModeScores("Single");
        data = new String[treeSet.size()][];
        int iterator = 0;
        for (Score value : treeSet) {
            String[] array = {value.getUserId(), value.getScore().toString()};
            data[iterator++] = array;
        }
        databaseManager.closeDatabase();
    }

    private void getMultiPlayerDB() {
        DatabaseManager databaseManager = new DatabaseManager("test");
        TreeSet<Score> treeSet = databaseManager.getGameModeScores("Multi");
        data = new String[treeSet.size()][];
        int iterator = 0;
        for (Score value : treeSet) {
            String[] array = {value.getUserId(), value.getScore().toString()};
            data[iterator++] = array;
        }
        databaseManager.closeDatabase();
    }
}