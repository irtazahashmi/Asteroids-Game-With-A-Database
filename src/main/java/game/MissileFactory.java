package game;

public class MissileFactory {

    /**
     * Method for creating missile objects.
     * @param player creating object
     * @param angle for missile
     * @return new missile
     */
    public static Missile createMissile(GameObject player, double angle) {
        return new Missile(player, angle);
    }
}