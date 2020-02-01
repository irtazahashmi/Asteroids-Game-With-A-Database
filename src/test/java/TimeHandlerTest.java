import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import game.TimeHandler;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for TimeHandler class.
 */
public class TimeHandlerTest {

    private transient TimeHandler timeHandler;

    /**
     * setUp method calling before each test.
     */
    @BeforeEach
    void setupTestEnvironment() {
        timeHandler = new TimeHandler(1000.0f);
    }

    /**
     * Test for setter of number of cycles per second.
     */
    @Test
    void setNumberOfCyclesPerSecondTest() {
        timeHandler.setNumberOfCyclesPerSecond(2.0f);
        assertEquals(timeHandler.getMillisecondsPerCycle(), 500);
    }

    /**
     * Test for setter of pausing time handler.
     */
    @Test
    void setPausedTest() {
        assertFalse(timeHandler.isPaused());
        timeHandler.setPaused(true);
        assertTrue(timeHandler.isPaused());
    }

    /**
     * Test for checking whether cycle is gone or not.
     * @throws InterruptedException when the thread is interrupted.
     */
    @Test
    void isCycleGoneTest() throws InterruptedException {
        assertFalse(timeHandler.isCycleGone());
        timeHandler.update();
        TimeUnit.SECONDS.sleep(20);
        timeHandler.update();
        assertTrue(timeHandler.isCycleGone());
    }

    //    /**
    //     * Test for updating time handler.
    //     */
    //     @Test
    //     void updateTest() {
    //         timeHandler.setPaused(true);
    //         timeHandler.update();
    //         assertEquals(timeHandler.getLastUpdatedAt(), System.nanoTime() / 1000000L);
    //     }
}
