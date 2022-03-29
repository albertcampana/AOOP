package nl.rug.aoop.asteroids.networking;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Class for the command data
 */
@Getter
@Setter
public class CommandData implements Serializable {

    private Integer lastCommand;

    private Boolean stop;

    /**
     * Constructor of the Command Data
     * @param lastCommand is the number of the last command executed
     * @param stop boolean that states if it has to stop or not
     */
    public CommandData(Integer lastCommand, Boolean stop) {
        this.lastCommand = lastCommand;
        this.stop = stop;
    }
}
