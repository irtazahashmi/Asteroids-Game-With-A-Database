package gui;

import game.Asteroid;
import game.GameObject;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.io.File;

@SuppressWarnings({"PMD.DataflowAnomalyAnalysis"})
public class GameLogic {

    /**
     * The frames per second.
     */
    public static final int FRAMES_PER_SECOND = 60;

    /**
     * The frames time.
     */
    public static final long FRAME_TIME = (long)(1000000000.0 / FRAMES_PER_SECOND);

    /**
     * The max number of levels.
     */
    public static final int MAXIMUM_LEVEL = 100;

    /**
     * The cool down time after death.
     */
    public static final int TIME_AFTER_DEATH = 200;

    /**
     * The time for respawning.
     */
    public static final int TIME_FOR_RESPAWNING = 100;

    /**
     * The time for invulnerable after death.
     */
    public static final int TIME_OF_BEING_INVULNERABLE_AFTER_DEATH = 0;

    /**
     * The reset time.
     */
    public static final int RESET_TIME = 120;

    /**
     * Increase asteroids by 3 after each level increases.
     */
    public static final int INCREASE_ASTEROIDS_BY = 3;

    /**
     * Are all the asteroids destroyed in the game.
     * @return a boolean whether all the asteroids destroyed in the game.
     */
    public static boolean areAllAsteroidsDestroyed() {
        for (GameObject e : Game.existingObjects) {
            if (e.getClass() == Asteroid.class) {
                return false;
            }
        }
        return true;
    }

    public static void playSoundEffect(String filepath) {
        try {
            AudioInputStream audioInputStream = AudioSystem
                    .getAudioInputStream(new File(filepath));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Help, can't music file!");
        }
    }

    /**
     * Play the missile sound effect.
     * @param filepath the filepath to the sound clip.
     */
    public static void playMissileSoundEffect(String filepath) {
        if (Game.playerRocket.isFiringAllowed()) {
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
    }

    /**
     * Should we restart the game.
     * @return boolean of whether to restart the game.
     */
    public static boolean shouldRestartGame() {
        boolean canRestart = (Game.isTheGameOver && Game.restartTime <= 0);
        if (canRestart) {
            Game.haveToRestartGame = true;
        }
        return canRestart;
    }
}
