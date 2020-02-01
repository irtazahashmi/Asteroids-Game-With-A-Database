import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import game.SizeOfAsteroid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for SizeOfAsteroid class.
 */
@SuppressWarnings("PMD.BeanMembersShouldSerialize")
public class SizeOfAsteroidTest {

    private SizeOfAsteroid smallAsteroid;
    private SizeOfAsteroid mediumAsteroid;
    private SizeOfAsteroid largeAsteroid;

    /**
     * setUp method calling before each test.
     */
    @BeforeEach
    void setupTestEnvironment() {
        smallAsteroid = SizeOfAsteroid.SmallAsteroid;
        mediumAsteroid = SizeOfAsteroid.MediumAsteroid;
        largeAsteroid = SizeOfAsteroid.LargeAsteroid;
    }

    /**
     * Test for constructor of small Asteroid.
     */
    @Test
    void smallConstructorTest() {
        assertNotNull(smallAsteroid);
        assertEquals(smallAsteroid.asteroid.getBounds(),
                SizeOfAsteroid.drawAsteroid(17.0).getBounds());
        assertEquals(smallAsteroid.radius, 17.0);
        assertEquals(smallAsteroid.points, 100);
    }

    /**
     * Test for constructor of medium Asteroid.
     */
    @Test
    void mediumConstructorTest() {
        assertNotNull(mediumAsteroid);
        assertEquals(mediumAsteroid.asteroid.getBounds(),
                SizeOfAsteroid.drawAsteroid(35.0).getBounds());
        assertEquals(mediumAsteroid.radius, 35.0);
        assertEquals(mediumAsteroid.points, 50);
    }

    /**
     * Test for constructor of large Asteroid.
     */
    @Test
    void largeConstructorTest() {
        assertNotNull(largeAsteroid);
        assertEquals(largeAsteroid.asteroid.getBounds(),
                SizeOfAsteroid.drawAsteroid(45.0).getBounds());
        assertEquals(largeAsteroid.radius, 45.0);
        assertEquals(largeAsteroid.points, 25);
    }
}
