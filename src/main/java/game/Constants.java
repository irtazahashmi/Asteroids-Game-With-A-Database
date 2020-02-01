package game;

import gui.GameScene;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.io.File;
import java.util.Random;

public class Constants {

    /**
     * The minimum rotation of the asteroid.
     */
    public static final double MINIMUM_ROTATION = 0.0075;
    /**
     * The maximum rotation of the asteroid.
     */
    public static final double MAXIMUM_ROTATION = 0.0175;
    /**
     * The difference in rotation of the asteroid.
     */
    public static final double DIFFERENCE_IN_ROTATION = MAXIMUM_ROTATION - MINIMUM_ROTATION;
    /**
     * The minimum velocity of the asteroid.
     */
    public static final double MINIMUM_VELOCITY = 0.8;
    /**
     * The maximum velocity of the asteroid.
     */
    public static final double MAXIMUM_VELOCITY = 2;
    /**
     * The difference in velocity of the asteroid.
     */
    public static final double VELOCITY_DIFFERENCE = MAXIMUM_VELOCITY - MINIMUM_VELOCITY;
    /**
     * The minimum distance the asteroid should travel.
     */
    public static final double MINIMUM_TRAVEL_DISTANCE = 200.0;
    /**
     * The maximum distance the asteroid should travel.
     */
    public static final double MAXIMUM_TRAVEL_DISTANCE = GameScene.SCREEN_SIZE / 2.0;
    /**
     * The difference in distance the asteroid should travel.
     */
    public static final double TRAVEL_DISTANCE_DIFFERENCE =
            MAXIMUM_TRAVEL_DISTANCE - MINIMUM_TRAVEL_DISTANCE;

    public static final float NUMBER_OF_UPDATES_TO_DO_AFTER_SPAWNING = 10;

    /**
     * Playing the sound effect of explosion when an asteroid is destroyed.
     * @param filepath filepath of the sound clip.
     */
    public static void playExplosionSoundEffect(String filepath) {
        try {
            AudioInputStream audioInputStream = AudioSystem
                    .getAudioInputStream(new File(filepath));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
            clip.start();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Help, can't find music file!");
        }
    }

    /**
     * Calaculate the initial velocity of the asteroid.
     * @param random random value
     * @return a vector which is the position of the asteroid.
     */
    public static Vector calculateVelocity(Random random) {
        Vector vector = new Vector(random.nextDouble() * Math.PI * 2);
        Vector velocityOfVector = vector.scaleBy(
                Constants.MINIMUM_VELOCITY + random.nextDouble() * Constants.VELOCITY_DIFFERENCE);
        return velocityOfVector;
    }
}
