package game;

import java.util.Random;

public class AsteroidFactory {

    /**
     * Method for creating new asteroid objects.
     * @param parent for asteroid
     * @param asteroidSize size of asteroid
     * @param random number of asteroid
     * @return new asteroid
     */
    public static Asteroid createAsteroid(Asteroid parent,
                                          SizeOfAsteroid asteroidSize, Random random) {
        if (parent == null) {
            return new Asteroid(random);
        }
        return new Asteroid(parent, asteroidSize, random);
    }
}