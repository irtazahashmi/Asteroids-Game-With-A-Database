import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import game.Asteroid;
import game.AsteroidFactory;
import game.Missile;
import game.MissileFactory;
import game.Rocket;
import game.RocketFactory;
import game.Vector;
import gui.Game;
import gui.GameScene;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

@SuppressWarnings("PMD.BeanMembersShouldSerialize")
class RocketTest {

    private Rocket rocket;
    private Rocket mockRocket;
    private Asteroid asteroid;
    private Missile missile;
    private Game game;
    private Graphics2D graphics2D;

    @BeforeEach
    void setupTestEnvironment() {
        rocket = RocketFactory.createRocket();
        asteroid = AsteroidFactory.createAsteroid(null, null, new Random());
        missile = MissileFactory.createMissile(asteroid, 30);
        game = Mockito.mock(Game.class);
        mockRocket = Mockito.mock(Rocket.class);
        graphics2D = Mockito.mock(Graphics2D.class);
    }

    @Test
    void constructorTest() {
        assertNotNull(rocket);
        assertFalse(rocket.isThrustActive());
        assertFalse(rocket.isRotateLeftActive());
        assertFalse(rocket.isRotateRightActive());
        assertFalse(rocket.isFireActive());
        assertTrue(rocket.isFiringAllowed());
    }

    @Test
    void setTrustingTest() {
        assertFalse(rocket.isThrustActive());
        rocket.setThrusting(true);
        assertTrue(rocket.isThrustActive());
    }

    @Test
    void setFiringTest() {
        assertFalse(rocket.isFireActive());
        rocket.setFiring(true);
        assertTrue(rocket.isFireActive());
    }

    @Test
    void rotateLeftActiveTest() {
        assertFalse(rocket.isRotateLeftActive());
        rocket.setRotateLeft(true);
        assertTrue(rocket.isRotateLeftActive());
    }

    @Test
    void rotateRightActiveTest() {
        assertFalse(rocket.isRotateRightActive());
        rocket.setRotateRight(true);
        assertTrue(rocket.isRotateRightActive());
    }

    @Test
    void setFiringAllowedTest() {
        assertTrue(rocket.isFiringAllowed());
        rocket.setFiringAllowed(false);
        assertFalse(rocket.isFiringAllowed());
    }

    @Test
    void resetTest() {
        rocket.reset();
        assertEquals(rocket.getObjectPosition().axis1, GameScene.SCREEN_SIZE / 2.0);
        assertEquals(rocket.getObjectPosition().axis2, GameScene.SCREEN_SIZE / 2.0);
        assertEquals(rocket.getObjectVelocity().axis1, 0.0);
        assertEquals(rocket.getObjectVelocity().axis2, 0.0);
        assertTrue(rocket.getMissiles().isEmpty());
    }

    @Test
    void getterTest() {
        assertEquals(rocket.getObjectPosition().axis1, 350.0);
        assertEquals(rocket.getObjectPosition().axis2, 350.0);
        assertEquals(rocket.getObjectRotation(), -Math.PI / 2.0);
        assertEquals(rocket.getObjectRadius(), 10.0);
    }

    @Test
    void collidedTest() {
        assertFalse(rocket.didCollide(asteroid));
        assertTrue(rocket.didCollide(rocket));
    }

    @Test
    void updateGameTest() {
        int animationFrame = rocket.getAnimationFrame();
        rocket.updateGame(game);
        assertEquals(rocket.getAnimationFrame(), animationFrame + 1);
    }

    @Test
    void updateThrustTest() {
        Vector velocity = rocket.getObjectVelocity();
        velocity.add(new Vector(rocket.getObjectVelocity()).scaleBy(0.0385));
        rocket.setThrusting(true);
        rocket.updateGame(game);
        assertEquals(rocket.getObjectVelocity(), velocity);
    }

    @Test
    void updateGameRightTest() {
        Rocket testRocket = RocketFactory.createRocket();
        testRocket.rotate(0.05);
        rocket.setRotateRight(true);
        rocket.updateGame(game);
        assertEquals(rocket.getObjectRotation(), testRocket.getObjectRotation());
    }

    @Test
    void updateGameLeftTest() {
        Rocket testRocket = RocketFactory.createRocket();
        testRocket.rotate(-0.05);
        rocket.setRotateLeft(true);
        rocket.updateGame(game);
        assertEquals(rocket.getObjectRotation(), testRocket.getObjectRotation());
    }

    @Test
    void drawObjectTest() {
        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = image.createGraphics();
        rocket.drawObject(graphics2D, game);
        assertNotNull(rocket);
    }

    @Test
    void otherDrawObjectTest() {
        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = image.createGraphics();
        when(game.isPaused()).thenReturn(false);
        rocket.setThrusting(true);
        rocket.drawObject(graphics2D, game);
        assertNotNull(rocket);
    }

    @Test
    void drawObjectElseTest() {
        when(game.isPlayerInvulnerable()).thenReturn(true);
        when(game.isPaused()).thenReturn(true);
        rocket.setAnimationFrame(15);
        rocket.drawObject(graphics2D, game);
        verify(graphics2D).drawOval(5, -5, 30, 10);
    }

    @Test
    void drawObjectTest2() {
        when(game.isPlayerInvulnerable()).thenReturn(true);
        when(game.isPaused()).thenReturn(false);
        rocket.setAnimationFrame(17);
        rocket.drawObject(graphics2D, game);
        verifyZeroInteractions(graphics2D);
    }

    @Test
    void collisionHandlerTest() {
        when(game.getPlayerLives()).thenReturn(0);
        rocket.collisionHandler(game, asteroid);
        assertEquals(game.getPlayerLives(), 0);
    }

    @Test
    void updateNegativeTest() {
        rocket.setObjectPosition(new Vector(-1, -1));
        rocket.updateGame(game);
        assertEquals(rocket.getObjectPosition().axis1, 699.0);
        assertEquals(rocket.getObjectPosition().axis2, 699.0);
    }

    @Test
    void collisionHandlerMissileTest() {
        rocket.collisionHandler(game, missile);
        assertFalse(game.isPaused());
    }

    @Test
    void rocketFactoryTest() {
        RocketFactory test = new RocketFactory();
        assertNotNull(test);
    }

    @Test
    void unnecessaryMissilesTest() {
        missile.setAlive(true);
        List<Missile> missiles = new ArrayList<>();
        missiles.add(missile);
        rocket.setMissiles(missiles);
        rocket.removeUnnecessaryMissiles();
        assertEquals(rocket.getMissiles().size(), 0);
    }

    @Test
    void necessaryMissilesTest() {
        missile.setAlive(false);
        List<Missile> missiles = new ArrayList<>();
        missiles.add(missile);
        rocket.setMissiles(missiles);
        rocket.removeUnnecessaryMissiles();
        assertEquals(rocket.getMissiles().size(), 1);
    }
}
