package Flomezy.pMaster.timedEvents;

import java.time.Duration;

public interface TimedEvent {

    /**
     * Starts the task of the underlying event if the task fails
     * this returns false.
     * @return false if the task fails, true if it succeeds.
     */
    public boolean eventTask();

    /**
     * Retreives the timing of the underlying event, which was set at
     * construction of the Object.
     * @return time interval of the event in seconds.
     */
    public Duration getDurationMs();


    /**
     * Retreives the time left until the next {@code eventTask()} is called
     * @return time left until next event trigger.
     */
    public int getTimeUntilEventS();

    /**
     * Prints parameter to the console with plugin and Event tag.
     * @param msg
     */
    public void printToConsole(String msg);

    public String toString();
}
