package game;

import java.awt.Polygon;

/**
 * A class for defining different asteroids size.
 */
@SuppressWarnings("PMD.DataflowAnomalyAnalysis")
public enum SizeOfAsteroid {

    /**
     * SmallAsteroid asteroid.
     */
    SmallAsteroid(17.0, 100),

    /**
     * Medium game.Asteroid asteroid.
     */
    MediumAsteroid(35.0, 50),

    /**
     * Large game.Asteroid asteroid.
     */
    LargeAsteroid(45.0, 25);


    private static final int POINTS = 5;

    /**
     * Shape of the asteroid.
     */
    public final Polygon asteroid;

    /**
     * Radius of asteroid.
     */
    public final double radius;

    /**
     * Points gained by destroying an asteroid.
     */
    public final int points;

    /**
     * Size of Asteroid constructor.
     * @param radius radius of asteroid
     * @param value points associated with it
     */
    SizeOfAsteroid(double radius, int value) {
        this.asteroid = drawAsteroid(radius);
        this.radius = radius;
        this.points = value;
    }

    /**
     * Drawing an asteroid.
     * @param radius radius of asteroid.
     * @return polygon shaped asteroid.
     */
    public static Polygon drawAsteroid(double radius) {
        int[] locationX = new int[POINTS];
        int[] locationY = new int[POINTS];

        double angle = (2 * Math.PI / POINTS);

        for (int i = 0; i < POINTS; i++) {
            locationX[i] = (int) (radius * Math.sin(i * angle));
            locationY[i] = (int) (radius * Math.cos(i * angle));
        }

        Polygon asteroid = new Polygon(locationX, locationY, POINTS);
        return asteroid;
    }
}
