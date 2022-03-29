package nl.rug.aoop.asteroids.database;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.nio.file.Path;
import java.util.TreeSet;

/**
 * Class that represents a database manager
 */
public class DatabaseManager {

    /**
     * Factory design pattern
     */
    private EntityManagerFactory managerFactory;

    /**
     * Manager of the database.
     */
    private EntityManager manager;

    /**
     * Class constructor
     *
     * @param dbName Name of the database
     */
    public DatabaseManager(String dbName) {
        initDatabase(dbName);
    }

    private void initDatabase(String dbName) {
        Path dbPath = Path.of("db", dbName + ".odb");
        managerFactory = Persistence.createEntityManagerFactory(dbPath.toString());
        manager = managerFactory.createEntityManager();
    }

    /**
     * Method that closes the database
     */
    public void closeDatabase() {
        manager.close();
        managerFactory.close();
    }

    /**
     * Method that adds a new Score
     *
     * @param score new score to be added
     */
    public void addScore(Score score) {
        manager.getTransaction().begin();
        manager.persist(score);
        manager.getTransaction().commit();
    }

    /**
     * Method that return all scores of a certain game mode
     *
     * @param gameMode game mode
     * @return a list with all the scores
     */
    public TreeSet<Score> getGameModeScores(String gameMode) {
        var query = manager.createQuery(
                "SELECT m FROM Score m WHERE m.gameMode='" + gameMode.toLowerCase() + "'", Score.class);
        return new TreeSet<>(query.getResultList());
    }
}
