package Flomezy.pMaster.timedEvents;

import java.time.Duration;

public class TimedUpdate implements TimedEvent{
    @Override
    public boolean eventTask() {
        return false;
    }

    @Override
    public Duration getDurationMs() {
        return null;
    }


    @Override
    public int getTimeUntilEventS() {
        return 0;
    }

    @Override
    public void printToConsole(String msg) {

    }
}
