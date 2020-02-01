import static org.junit.jupiter.api.Assertions.assertEquals;

import game.Vector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for Vector class.
 */
class VectorTest {
    private transient Vector vector1;
    private transient Vector vector2;

    /**
     * setUp method calling before each test.
     */
    @BeforeEach
    void setupTestEnvironment() {
        vector1 = new Vector(3.0, 4.0);
        vector2 = new Vector(4.0, 5.0);
    }

    /**
     * Test for normalizing vectors.
     */
    @Test
    void normalizeTest() {
        vector1.normalize();
        assertEquals(vector1.axis1, 0.6);
        assertEquals(vector1.axis2, 0.8);
    }

    /**
     * Test for normalizing vectors.
     */
    @Test
    void normalizeOtherTest() {
        Vector oneVector = new Vector(0.6, 0.8);
        Vector zeroVector = new Vector(0.0, 0.0);
        oneVector.normalize();
        zeroVector.normalize();
        assertEquals(oneVector.axis1, 0.6);
        assertEquals(oneVector.axis2, 0.8);
        assertEquals(zeroVector.axis1, 0.0);
        assertEquals(zeroVector.axis2, 0.0);
    }

    /**
     * Test for calculating the squares of distance between vectors.
     */
    @Test
    void getDistanceToSquaredTest() {
        double result = vector1.getDistanceToSquared(vector2);
        assertEquals(result, 2.0);
    }

    /**
     * Test for the set up method.
     */
    @Test
    void setTest() {
        vector1.set(5.0, 10.0);
        assertEquals(5.0, vector1.axis1);
        assertEquals(10.0, vector1.axis2);
    }

    /**
     * Test for the add method.
     */
    @Test
    void addTest() {
        vector1.add(vector2);
        assertEquals(7.0, vector1.axis1);
        assertEquals(9.0, vector1.axis2);
    }

    /**
     * Test for the scale method.
     */
    @Test
    void scaleByTest() {
        vector1.scaleBy(3.0);
        assertEquals(9.0, vector1.axis1);
        assertEquals(12.0, vector1.axis2);
    }

    /**
     * Constructor angle test.
     */
    @Test
    void constructorAngleTest() {
        Vector vector3 = new Vector(0.0);
        assertEquals(1.0, vector3.axis1);
        assertEquals(0.0, vector3.axis2);
    }

    /**
     * Constructor vector test.
     */
    @Test
    void constructorVectorTest() {
        Vector vector3 = new Vector(vector1);
        assertEquals(3.0, vector3.axis1);
        assertEquals(4.0, vector3.axis2);
    }
}
