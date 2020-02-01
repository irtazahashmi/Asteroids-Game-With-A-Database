package game;

import gui.Game;
import gui.GameScene;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * The rocket (player) class.
 */
@SuppressWarnings({"PMD.BeanMembersShouldSerialize", "PMD.AvoidLiteralsInIfCondition"})
public class Rocket extends GameObject {

    /**
     * The rotation of the rocket.
     */
    private static final double ROTATION = -Math.PI / 2.0;

    /**
     * The thrust of the rocket.
     */
    private static final double THRUST = 0.06;

    /**
     * The maximum velocity of the rocket.
     */
    private static final double MAXIMUM_VELOCITY = 6.5;

    /**
     * The maximum speed of the rocket.
     */
    private static final double MAXIMUM_ROTATION_SPEED = 0.05;
    /**
     * The deacceleration speed of the rocket.
     */
    private static final double DEACCELERATION = 0.99;
    /**
     * The maximum number of missiles the rocket can fire at once.
     */
    private static final int MAXIMUM_MISSILES = 5;

    /**
     * The maximum number of missiles the rocket can fire at consecutively.
     */
    private static final int FIRE_RATE = 5;

    /**
     * The maximum number of missiles the rocket can fire at consecutively.
     */
    private static final int MAXIMUM_CONSECUTIVE_SHOTS = 5;

    /**
     * The time that the rocket needs to cool down once shot 5
     * consecutive missiles.
     */
    private static final int OVERHEAT_TIME = 30;

    /**
     * Is the thrust active.
     */
    private boolean thrustActive;
    /**
     * Is left rotation active.
     */
    private boolean rotateLeftActive;
    /**
     * Is right rotation active.
     */
    private boolean rotateRightActive;
    /**
     * Is fire active.
     */
    private boolean fireActive;
    /**
     * Is firing allowed.
     */
    private boolean firingAllowed;

    /**
     * Number of consecutive shots.
     */
    private int consecutiveShots;
    /**
     * Cooldown time.
     */
    private int fireCooldown;
    /**
     * Overheat cool down time.
     */
    private int overheatCooldownTime;
    /**
     * Animation frame for the thrust when thrust is active.
     */
    private int animationFrame;

    /**
     * List of missiles the rocket can shoot.
     */
    private List<Missile> missiles;

    public void setMissiles(List<Missile> missiles) {
        this.missiles = missiles;
    }

    /**
     * The constructor method.
     */
    public Rocket() {
        super(new Vector(GameScene.SCREEN_SIZE / 2.0, GameScene.SCREEN_SIZE / 2.0),
                new Vector(0.0, 0.0), 10.0, 0);
        this.missiles = new ArrayList<>();
        this.objectRotation = ROTATION;
        this.thrustActive = false;
        this.rotateLeftActive = false;
        this.rotateRightActive = false;
        this.fireActive = false;
        this.firingAllowed = true;
        this.fireCooldown = 0;
        this.overheatCooldownTime = 0;
        this.animationFrame = 0;
    }

    /**
     * Setter for the thruster.
     * @param state does or does not have thrusters.
     */
    public void setThrusting(boolean state) {
        this.thrustActive = state;
    }

    /**
     * Setter for rotating left.
     * @param state can or can not rotate left.
     */
    public void setRotateLeft(boolean state) {
        this.rotateLeftActive = state;
    }

    /**
     * Setter for rotating right.
     * @param state can or can not rotate right.
     */
    public void setRotateRight(boolean state) {
        this.rotateRightActive = state;
    }

    /**
     * Setter for firing.
     * @param state can or can not fire.
     */
    public void setFiring(boolean state) {
        this.fireActive = state;
    }

    /**
     * Setter for firing.
     * @param state can or can not fire.
     */
    public void setFiringAllowed(boolean state) {
        this.firingAllowed = state;
    }

    /**
     * The method resets the game.
     */
    public void reset() {
        this.objectRotation = ROTATION;
        objectPosition.set(GameScene.SCREEN_SIZE / 2.0, GameScene.SCREEN_SIZE / 2.0);
        objectVelocity.set(0.0, 0.0);
        missiles.clear();
    }

    /**
     * Update the game.
     * @param game current game.
     */

    @Override
    public void updateGame(Game game) {
        super.updateGame(game);
        this.animationFrame++;
        moveRocket();
        shouldNegativelyAccelerate(objectVelocity.getLengthSquared() != 0.0);
        removeUnnecessaryMissiles();
        this.fireCooldown--;
        this.overheatCooldownTime--;
        fireMissiles(game);
    }

    /**
     * Moving rocket.
     */
    public void moveRocket() {
        if (rotateLeftActive != rotateRightActive) {
            if (rotateLeftActive) {
                rotate(-MAXIMUM_ROTATION_SPEED);
            } else {
                rotate(MAXIMUM_ROTATION_SPEED);
            }
        }

        if (thrustActive) {
            objectVelocity.add(new Vector(objectRotation).scaleBy(THRUST));

            if (objectVelocity.getLengthSquared() >= MAXIMUM_VELOCITY * MAXIMUM_VELOCITY) {
                objectVelocity.normalize().scaleBy(MAXIMUM_VELOCITY);
            }
        }
    }

    /**
     * Firing missiles from the rocket.
     * @param game current game.
     */
    public void fireMissiles(Game game) {
        if (firingAllowed && fireActive && fireCooldown <= 0 && overheatCooldownTime <= 0) {
            if (missiles.size() < MAXIMUM_MISSILES) {
                this.fireCooldown = FIRE_RATE;

                Missile bullet = MissileFactory.createMissile(this, objectRotation);
                missiles.add(bullet);
                game.registerEntity(bullet);
            }

            this.consecutiveShots++;
            if (consecutiveShots == MAXIMUM_CONSECUTIVE_SHOTS) {
                this.consecutiveShots = 0;
                this.overheatCooldownTime = OVERHEAT_TIME;
            }
        } else if (consecutiveShots > 0) {
            this.consecutiveShots--;
        }
    }

    /**
     * Removing bullets that have been fired.
     */
    public void removeUnnecessaryMissiles() {
        Iterator<Missile> missileIterator = missiles.iterator();
        if (missileIterator.hasNext()) {
            do {
                GameObject missile = missileIterator.next();
                if (missile.needsRemoval()) {
                    missileIterator.remove();
                }
            } while (missileIterator.hasNext());
        }
    }

    /**
     * Checking whether the rocket should negatively accelerate.
     * @param shouldApply true or false
     */
    private void shouldNegativelyAccelerate(boolean shouldApply) {
        if (shouldApply) {
            objectVelocity.scaleBy(DEACCELERATION);
        }
    }


    /**
     * Handle collisions.
     * @param game current game.
     * @param other the other game object the object collides with.
     */
    @Override
    public void collisionHandler(Game game, GameObject other) {
        if (other.getClass() == Asteroid.class) {
            game.playerDies();
        }
    }


    /**
     * Drawing the rocket object.
     * @param graphics2D the graphics object
     * @param game the current game
     */
    @Override
    public void drawObject(Graphics2D graphics2D, Game game) {

        if (!game.isPlayerInvulnerable() || game.isPaused() || animationFrame % 20 < 10) {

            //main body
            graphics2D.drawOval(5, -5, 30, 10);
            //window
            graphics2D.drawOval(25, -2, 3, 3);
            //right
            graphics2D.drawOval(5, 5, 20, 5);
            //left
            graphics2D.drawOval(5, -10, 20, 5);
            //Fire thrust animation when the rocket is moving
            if (!game.isPaused() && thrustActive && animationFrame % 6 < 3) {
                graphics2D.drawLine(-6, -6, -12, 0);
                graphics2D.drawLine(-6, 6, -12, 0);
                graphics2D.drawLine(-6, -6, -6, 6);
            }
        }
    }

    /**
     * Getter for the missiles.
     * @return List of missiles.
     */
    public List<Missile> getMissiles() {
        return missiles;
    }

    /**
     * Is firing allowed.
     * @return boolean of whether it is allowed.
     */
    public boolean isFiringAllowed() {
        return firingAllowed;
    }

    /**
     * Is firing active.
     * @return boolean of whether it is active.
     */
    public boolean isFireActive() {
        return fireActive;
    }

    /**
     * Is rotating right active.
     * @return boolean of whether it is active.
     */
    public boolean isRotateRightActive() {
        return rotateRightActive;
    }

    /**
     * Is rotating left active.
     * @return boolean of whether it is active.
     */
    public boolean isRotateLeftActive() {
        return rotateLeftActive;
    }

    /**
     * Is thrusters active.
     * @return boolean of whether it is active.
     */
    public boolean isThrustActive() {
        return thrustActive;
    }

    public int getAnimationFrame() {
        return animationFrame;
    }

    public void setAnimationFrame(int animationFrame) {
        this.animationFrame = animationFrame;
    }
}
