package nl.rug.aoop.asteroids.util;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Class that represents a pair of elements
 *
 * @param <F> first element of the pair
 * @param <S> second element of the pair
 */
@Getter
@Setter
public class Pair<F, S> implements Serializable {
    /**
     * First element of the pair
     */
    private F a;

    /**
     * Second element of the pair
     */
    private S b;

    /**
     * Constructor for a Pair.
     *
     * @param a the first object in the Pair
     * @param b the second object in the pair
     */
    public Pair(F a, S b) {
        this.a = a;
        this.b = b;
    }
}