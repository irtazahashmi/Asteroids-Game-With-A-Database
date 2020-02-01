package game;

/**
 * This class will be used to keep track of the objects in
 * 2D space.
 */

@SuppressWarnings("PMD.BeanMembersShouldSerialize")
public class Vector {

    /**
     * X location.
     */
    public double axis1;
    /**
     * Y location.
     */
    public double axis2;


    /**
     * Creating a vector from an angle.
     * @param angle - given angle.
     */

    public Vector(double angle) {
        this.axis1 = Math.cos(angle);
        this.axis2 = Math.sin(angle);
    }

    /**
     * Creates a vector from x and y location.
     * @param axis1 x axis
     * @param axis2 y axis
     */

    public Vector(double axis1, double axis2) {
        this.axis1 = axis1;
        this.axis2 = axis2;
    }

    /**
     * Creating a new vector from an old one.
     * @param vector - vector to copy.
     */

    public Vector(Vector vector) {
        this.axis1 = vector.axis1;
        this.axis2 = vector.axis2;
    }

    /**
     * Setting the component of this vector.
     * @param axis1 - location X
     * @param axis2 - location Y
     * @return - a vector
     */
    public Vector set(double axis1, double axis2) {
        this.axis1 = axis1;
        this.axis2 = axis2;
        return this;
    }

    /**
     * Adding vectors.
     * @param vector - game.Vector that you wanna add.
     * @return summed vector
     */
    public Vector add(Vector vector) {
        this.axis1 += vector.axis1;
        this.axis2 += vector.axis2;
        return this;
    }

    /**
     * Scaling the vector.
     * @param scalar - the scalar you wanna scale with.
     * @return scaled vector
     */
    public Vector scaleBy(double scalar) {
        this.axis1 *= scalar;
        this.axis2 *= scalar;
        return this;
    }

    /**
     * Normalizing a vector.
     * @return - normalized vector.
     */
    public Vector normalize() {
        double lengthOfVector = getLengthSquared();
        if (lengthOfVector != 0.0f && lengthOfVector != 1.0f) {
            lengthOfVector = Math.sqrt(lengthOfVector);
            this.axis1 /= lengthOfVector;
            this.axis2 /= lengthOfVector;
        }
        return this;
    }

    /**
     * Getting the length of the vector and squaring it.
     * @return square length of a vector.
     */
    public double getLengthSquared() {
        return (axis1 * axis1 + axis2 * axis2);
    }

    /**
     * Distance between two vectors.
     * @param vector - vector to find distance between.
     * @return - the distance between the vectors
     */
    public double getDistanceToSquared(Vector vector) {
        double distanceX = this.axis1 - vector.axis1;
        double distanceY = this.axis2 - vector.axis2;
        double distance = Math.pow(distanceX, 2) + Math.pow(distanceY, 2);
        return distance;
    }

}
