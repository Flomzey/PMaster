package Flomezy.pMaster.timedEvents;

import java.time.Duration;

public interface TimedEvent {

    /**
     * Starts the task of the underlying event if the task fails
     * this returns false.
     * @return false if the task fails, true if it succeeds.
     */
    boolean eventTask();

    /**
     * Retrieves the timing of the underlying event, which was set at
     * construction of the Object.
     * @return time interval of the event in seconds.
     */
    int getDurationS();

    int getTimeUntilEventTaskS();

    void subtractTimeUntilEventTaskS();

    void resetTimeUnitlEventTaskS();

    /**
     * Prints parameter to the console with plugin and Event tag.
     * @param msg
     */
    void printToConsole(String msg);

    String toString();
}
