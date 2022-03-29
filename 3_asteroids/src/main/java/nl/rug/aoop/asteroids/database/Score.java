package nl.rug.aoop.asteroids.database;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Class that represents a score
 */
@Getter
@Setter
@Entity
public class Score implements Comparable {

    /**
     * id of the Score.
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * ID of the user.
     */
    private String userId;

    /**
     * Value of the score
     */
    private Integer score;

    /**
     * Game Mode that the score comes from
     */
    private String gameMode;

    /**
     * Empty constructor
     */
    public Score() {
    }

    /**
     * Constructor
     *
     * @param userId   player id
     * @param score    player points
     * @param gameMode game mode played
     */
    public Score(String userId, Integer score, String gameMode) {
        this.userId = userId;
        this.score = score;
        this.gameMode = gameMode.toLowerCase();
    }

    /**
     * Class comparator
     *
     * @param o object to be compared to
     * @return true if this is less than o
     */
    @Override
    public int compareTo(Object o) {
        Score s = (Score) o;
        if (this.score < s.getScore()) return 1;
        return -1;
    }
}
