package game;

import gui.Game;

import java.awt.Graphics2D;

/**
 * The missile class.
 */
public class Missile extends GameObject {

    /**
     * The velocity of the missile.
     */
    private static final double VELOCITY = 7.0;

    /**
     * The range of the missile.
     */
    private static final int RANGE = 65;

    /**
     * The range of the missile.
     */
    private int range;

    /**
     * Missile constructor.
     * @param player the player of the game.
     * @param angle the angle the missile is shot at.
     */
    public Missile(GameObject player, double angle) {
        super(new Vector(player.objectPosition), new Vector(angle).scaleBy(VELOCITY), 2.0, 0);
        this.range = RANGE;
    }

    /**
     * Update the game.
     * @param game current game.
     */
    @Override
    public void updateGame(Game game) {
        super.updateGame(game);

        this.range--;
        if (range <= 0) {
            isAlive();
        }
    }

    /**
     * Handle collisions.
     * @param game current game.
     * @param other object to collide with.
     */
    @Override
    public void collisionHandler(Game game, GameObject other) {
        if (other.getClass() != Rocket.class) {
            isAlive();
        }
    }

    /**
     * Draw the missile object.
     * @param graphics2D current graphics to paint it.
     * @param game current game.
     */
    @Override
    public void drawObject(Graphics2D graphics2D, Game game) {
        graphics2D.drawOval(-1, -1, 4, 4);
    }

    /**
     * Get the range of the missile.
     * @return the range.
     */
    public int getRange() {
        return range;
    }

    /**
     * Set the range of the missile.
     */
    public void setRange(int range) {
        this.range = range;
    }
}
