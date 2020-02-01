package game;

import gui.Game;
import gui.GameScene;

import java.awt.Graphics2D;
import java.io.File;
import java.util.Random;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JOptionPane;

/**
 * The asteroid class.
 */
@SuppressWarnings({"PMD.DataflowAnomalyAnalysis", "PMD.MissingSerialVersionUID", "Duplicates"})
public class Asteroid extends GameObject {

    /**
     * The size of the asteroid.
     */
    private transient SizeOfAsteroid asteroidSize;

    /**
     * The rotational speed of the asteroid.
     */
    private transient double rotationSpeed;

    /**
     * Getting the size of the asteroid.
     * @return asteroid size.
     */
    public SizeOfAsteroid getAsteroidSize() {
        return asteroidSize;
    }

    /**
     * Asteroid constructor.
     * @param random a random value
     */
    public Asteroid(Random random) {
        super(calculatePosition(random), Constants.calculateVelocity(random),
                SizeOfAsteroid.LargeAsteroid.radius, SizeOfAsteroid.LargeAsteroid.points);
        this.rotationSpeed = -Constants.MINIMUM_ROTATION + (random.nextDouble() * Constants.DIFFERENCE_IN_ROTATION);
        this.asteroidSize = SizeOfAsteroid.LargeAsteroid;
    }

    /**
     * The constructor creating new asteroid form a parent.
     * @param parent of asteroid
     * @param asteroidSize of asteroid
     * @param random of asteroid
     */
    public Asteroid(Asteroid parent, SizeOfAsteroid asteroidSize, Random random) {
        super(new Vector(parent.objectPosition), Constants.calculateVelocity(random),
                asteroidSize.radius, asteroidSize.points);
        this.rotationSpeed = Constants.MINIMUM_ROTATION + (random.nextDouble() * Constants.DIFFERENCE_IN_ROTATION);
        this.asteroidSize = asteroidSize;

        for (int i = 0; i < Constants.NUMBER_OF_UPDATES_TO_DO_AFTER_SPAWNING; i++) {
            updateGame(null);
        }
    }

    /**
     * Calculate the current initial position of the asteroid.
     * @param random a random number
     * @return a vector which is the position of the asteroid.
     */
    private static Vector calculatePosition(Random random) {
        double maximumDistanceX = GameScene.SCREEN_SIZE / 2.0;
        double maximumDistanceY = GameScene.SCREEN_SIZE / 2.0;
        Vector maximumVector =  new Vector(maximumDistanceX, maximumDistanceY);
        Vector randomVector = new Vector(random.nextDouble() * Math.PI * 2);
        Vector resultVector = maximumVector.add(randomVector);
        Vector positionVector = resultVector.scaleBy(
                Constants.MINIMUM_TRAVEL_DISTANCE + random.nextDouble() * Constants.TRAVEL_DISTANCE_DIFFERENCE);
        return positionVector;
    }

    /**
     * Update the game.
     * @param game current game.
     */
    @Override
    public void updateGame(Game game) {
        super.updateGame(game);
        rotate(rotationSpeed);
    }

    /**
     * Draw the asteroid object.
     * @param graphics2D the graphics used to paint the game.
     * @param game current game.
     */
    @Override
    public void drawObject(Graphics2D graphics2D, Game game) {
        graphics2D.drawPolygon(asteroidSize.asteroid);
        graphics2D.fillPolygon(asteroidSize.asteroid);
    }

    /**
     * Handle the collisions.
     * @param game current game.
     * @param other the other game object the asteroid is colliding with.
     */
    @Override
    public void collisionHandler(Game game, GameObject other) {
        if (other.getClass() != Asteroid.class) {
            if (asteroidSize != SizeOfAsteroid.SmallAsteroid) {
                SizeOfAsteroid spawnSize = SizeOfAsteroid.values()[asteroidSize.ordinal() - 1];
                for (int i = 0; i < 2; i++) {
                    game.registerEntity(AsteroidFactory.createAsteroid(this, spawnSize,
                            game.getRandomInstance()));
                }
            }

            isAlive();
            Constants.playExplosionSoundEffect("src/main/java/game/media/explosion.wav");
            game.addScore(getPoints());
        }
    }
}
