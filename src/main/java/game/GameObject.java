package game;

import gui.Game;
import gui.GameScene;

import java.awt.Graphics2D;

/**
 * The abstract class game objects.
 */
@SuppressWarnings({"PMD.BeanMembersShouldSerialize", "PMD.AvoidFieldNameMatchingMethodName",
        "PMD.AvoidLiteralsInIfCondition"})
public abstract class GameObject {

    /**
     * Position of the game object.
     */
    protected Vector objectPosition;
    /**
     * Velocity of the game object.
     */
    protected Vector objectVelocity;
    /**
     * Rotation of the game object.
     */
    protected double objectRotation;
    /**
     * Radius of the game object.
     */
    protected double objectRadius;
    /**
     * Is the game object alive.
     */
    private boolean isAlive;

    /**
     * Total points.
     */
    private int points;

    /**
     * Setter method for position.
     * @param objectPosition of the vector
     */
    public void setObjectPosition(Vector objectPosition) {
        this.objectPosition = objectPosition;
    }

    /**
     * The constructor method.
     * @param objectPosition of game.GameObject
     * @param objectVelocity of game.GameObject
     * @param objectRadius of game.GameObject
     * @param points of game.GameObject
     */
    public GameObject(Vector objectPosition, Vector objectVelocity,
                      double objectRadius, int points) {
        this.objectPosition = objectPosition;
        this.objectVelocity = objectVelocity;
        this.objectRadius = objectRadius;
        this.objectRotation = 0.0f;
        this.points = points;
        this.isAlive = false;
    }

    /**
     * Rotate the game object.
     * @param amount by the amount you wanna rotate.
     */
    public void rotate(double amount) {
        this.objectRotation += amount;
        this.objectRotation %= Math.PI * 2;
    }

    /**
     * Get points.
     * @return Number of points.
     */
    public int getPoints() {
        return points;
    }

    /**
     * Is the game object alive.
     */
    public void isAlive() {
        this.isAlive = true;
    }

    /**
     * Get game object position.
     * @return Vector position of the game object.
     */
    public Vector getObjectPosition() {
        return objectPosition;
    }

    /**
     * Get game object velocity.
     * @return Vector velocity of the game object.
     */
    public Vector getObjectVelocity() {
        return objectVelocity;
    }

    /**
     * Get game object rotation.
     * @return rotation of the game object.
     */
    public double getObjectRotation() {
        return objectRotation;
    }

    /**
     * Get game object radius.
     * @return radius of the game object.
     */
    public double getObjectRadius() {
        return objectRadius;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    /**
     * Does the object have to be removed from the game.
     * @return boolean of whether to remove the object or not.
     */
    public boolean needsRemoval() {
        return isAlive;
    }

    /**
     * The method updating the game.
     * @param game current
     */
    public void updateGame(Game game) {
        objectPosition.add(objectVelocity);

        if (objectPosition.axis1 < 0.0f) {
            objectPosition.axis1 += GameScene.SCREEN_SIZE;
        }
        if (objectPosition.axis2 < 0.0f) {
            objectPosition.axis2 += GameScene.SCREEN_SIZE;
        }
        objectPosition.axis1 %= GameScene.SCREEN_SIZE;
        objectPosition.axis2 %= GameScene.SCREEN_SIZE;
    }

    /**
     * Did the game object collide.
     * @param gameObject the game object.
     * @return boolean of did they collide or not.
     */
    public boolean didCollide(GameObject gameObject) {
        double radius = gameObject.getObjectRadius() + getObjectRadius();
        boolean collided = objectPosition.getDistanceToSquared(gameObject.objectPosition)
                < Math.pow(radius, 2);
        return collided;
    }

    /**
     * Handle collisions.
     * @param game current game.
     * @param other the other game object that collided with.
     */
    public abstract void collisionHandler(Game game, GameObject other);

    /**
     * draw game object.
     * @param graphics2D current graphics.
     * @param game current game.
     */
    public abstract void drawObject(Graphics2D graphics2D, Game game);
}
