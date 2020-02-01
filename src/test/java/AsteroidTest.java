import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import game.Asteroid;
import game.AsteroidFactory;
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

public class AsteroidTest {

    private transient Asteroid asteroid1;
    private transient Asteroid asteroid2;
    private transient Asteroid asteroidSmall;
    private transient Random random;
    private transient Rocket rocket;
    private transient Game game;

    @BeforeEach
    void setupTestEnvironment() {
        random = new Random();
        rocket = RocketFactory.createRocket();
        asteroid1 = AsteroidFactory.createAsteroid(null, null, new Random());
        asteroid2 = AsteroidFactory.createAsteroid(asteroid1, SizeOfAsteroid.LargeAsteroid, random);
        asteroidSmall = AsteroidFactory
                .createAsteroid(asteroid1, SizeOfAsteroid.SmallAsteroid, random);
        game = Mockito.mock(Game.class);
        when(game.getPlayerScore()).thenReturn(40);
        when(game.getRandomInstance()).thenReturn(random);
    }

    @Test
    void constructorTest() {
        assertNotNull(asteroid2);
        assertEquals(asteroid2.getAsteroidSize(), SizeOfAsteroid.LargeAsteroid);
    }

    @Test
    void drawObjectTest() {
        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = image.createGraphics();
        asteroid1.drawObject(graphics2D, game);
        assertNotNull(asteroid1);
    }

    // @Test
    // void collisionHandlerSmallTest() {
    //     asteroidSmall.collisionHandler(game, rocket);
    //     assertTrue(asteroidSmall.needsRemoval());
    // }

    // @Test
    // void collisionHandlerLargeTest() {
    //     asteroid2.collisionHandler(game, rocket);
    //     assertTrue(asteroid2.needsRemoval());
    // }

    @Test
    void collisionAsteroidTest() {
        asteroid1.collisionHandler(game, asteroid2);
        assertEquals(40, game.getPlayerScore());
    }

    @Test
    void asteroidFactoryTest() {
        AsteroidFactory test = new AsteroidFactory();
        assertNotNull(test);
    }
}