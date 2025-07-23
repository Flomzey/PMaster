package Flomezy.pMaster.timedEvents;

import java.time.Duration;

public class TimedUpdate implements TimedEvent{

    @Override
    public boolean eventTask() {
        return false;
    }

    @Override
    public int getDurationS() {
        return 0;
    }

    @Override
    public int getTimeUntilEventTaskS() {
        return 0;
    }

    @Override
    public void subtractTimeUntilEventTaskS() {

    }

    @Override
    public void resetTimeUnitlEventTaskS() {

    }

    @Override
    public void printToConsole(String msg) {

    }
}
