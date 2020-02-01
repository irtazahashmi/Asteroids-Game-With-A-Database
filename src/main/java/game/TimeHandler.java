package game;

/**
 * Keeping track of cycles throughout the game for updating the game
 * when needed.
 */
@SuppressWarnings({"PMD.BeanMembersShouldSerialize", "PMD.DataflowAnomalyAnalysis",
        "PMD.AvoidFieldNameMatchingMethodName", "PMD.ConstructorCallsOverridableMethod"})
public class TimeHandler {

    /**
     * Number of milliseconds gone per cycle.
     */
    private float millisecondsPerCycle;
    /**
     * When was the frame last updated.
     */
    private long lastUpdatedAt;
    /**
     * Number of cycles that are gone and haven't been registered.
     */
    private int goneCycles;
    /**
     * Extra cycles towards next gone cycle.
     */
    private float extraCycles;
    /**
     * Is the game paused.
     */
    private boolean isPaused;


    /**
     * Default constructor.
     * @param cyclesPerSecond number of cycles gone per sec.
     */
    public TimeHandler(float cyclesPerSecond) {
        setNumberOfCyclesPerSecond(cyclesPerSecond);
        reset();
    }

    public float getMillisecondsPerCycle() {
        return millisecondsPerCycle;
    }

    /**
     * Setting the number of cycles per second.
     * @param cyclesPerSecond Number of cycles per second.
     */
    public void setNumberOfCyclesPerSecond(float cyclesPerSecond) {
        this.millisecondsPerCycle = (1.0f / cyclesPerSecond) * 1000;
    }

    /**
     * Resetting the clock.
     */
    public void reset() {
        this.goneCycles = 0;
        this.extraCycles = 0.0f;
        this.lastUpdatedAt = getCurrentTime();
        this.isPaused = false;
    }

    public long getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    /**
     * Updating the clock.
     */
    public void update() {
        long currentTime = getCurrentTime();
        float changeInTime = (float)(currentTime - lastUpdatedAt) + extraCycles;

        if (!isPaused) {
            this.goneCycles += (int)Math.floor(changeInTime / millisecondsPerCycle);
            this.extraCycles = changeInTime % millisecondsPerCycle;
        }

        this.lastUpdatedAt = currentTime;
    }

    /**
     * Controlling whether the game is paused or not.
     * @param paused paused game or not.
     */
    public void setPaused(boolean paused) {
        this.isPaused = paused;
    }

    /**
     * Checking whether game paused or not.
     * @return Whether or not this clock is paused.
     */
    public boolean isPaused() {
        return isPaused;
    }

    /**
     * Checking whether a cycle has been gone.
     * @return Has a cycle passed or not.
     */
    public boolean isCycleGone() {
        if (goneCycles > 0) {
            this.goneCycles--;
            return true;
        }
        return false;
    }

    /**
     * Get current time from OS.
     * @return current time.
     */
    private static final long getCurrentTime() {
        return (System.nanoTime() / 1000000L);
    }
}
