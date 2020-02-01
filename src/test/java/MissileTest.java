import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import game.Asteroid;
import game.AsteroidFactory;
import game.Missile;
import game.MissileFactory;
import game.Rocket;
import game.RocketFactory;
import game.SizeOfAsteroid;
import gui.Game;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;


public class MissileTest {

    private transient Missile missile;
    private transient Asteroid asteroid;
    private transient Game game;
    private transient Rocket rocket;

    @BeforeEach
    void setupTestEnvironment() {
        asteroid = AsteroidFactory.createAsteroid(null, null, new Random());
        missile = MissileFactory.createMissile(asteroid, 2.0);
        rocket = RocketFactory.createRocket();
        game = Mockito.mock(Game.class);
    }

    @Test
    void constructorTest() {
        assertNotNull(missile);
        assertEquals(missile.getRange(), 65);
    }

    @Test
    void updateGameTest() {
        missile.setRange(1);
        assertEquals(missile.getRange(), 1);
        missile.updateGame(game);
        assertEquals(missile.getRange(), 0);
        assertTrue(missile.needsRemoval());
    }

    @Test
    void updateGameNoLifespanTest() {
        missile.setRange(3);
        missile.updateGame(game);
        assertEquals(missile.getRange(), 2);
        assertFalse(missile.needsRemoval());
    }

    @Test
    void getPointsTest() {
        assertEquals(missile.getPoints(), 0);
    }

    @Test
    void getSizeAsteroidTest() {
        assertEquals(asteroid.getAsteroidSize(), SizeOfAsteroid.LargeAsteroid);
    }

    @Test
    void rotateTest() {
        double rotate = missile.getObjectRotation();
        missile.rotate(5.0);
        double newRotation = (rotate + 5.0) % (Math.PI * 2);
        assertEquals(missile.getObjectRotation(), newRotation);
    }

    @Test
    void collisionHandlerTest() {
        missile.collisionHandler(game, asteroid);
        assertTrue(missile.needsRemoval());
    }

    @Test
    void collisionRocketTest() {
        missile.collisionHandler(game, rocket);
        assertFalse(missile.needsRemoval());
    }

    @Test
    void drawObjectTest() {
        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = image.createGraphics();
        missile.drawObject(graphics2D, game);
        assertNotNull(missile);
    }

    @Test
    void missileFactoryTest() {
        MissileFactory test = new MissileFactory();
        assertNotNull(test);
    }
}